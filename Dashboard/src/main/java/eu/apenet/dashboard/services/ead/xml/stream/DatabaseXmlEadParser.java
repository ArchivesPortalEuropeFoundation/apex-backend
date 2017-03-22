package eu.apenet.dashboard.services.ead.xml.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.database.EadDatabaseSaver;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadArchDescCLevelXpathReader;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadGlobalXpathReader;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadPublishData;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadSolrPublisher;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.xml.ApeXMLConstants;

public class DatabaseXmlEadParser {

    public static final QName ARCHDESC = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "archdesc");
    private static final Logger LOG = Logger.getLogger(DatabaseXmlEadParser.class);

    public static long publish(Ead ead) throws Exception {
        EadDatabaseSaver eadDatabaseSaver = new EadDatabaseSaver();
        CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
        EadContent eadContent = ead.getEadContent();
        List<LevelInfo> upperLevels = new ArrayList<LevelInfo>();
        ArchivalInstitution ai = ead.getArchivalInstitution();
        Map<String, Object> fullHierarchy = new HashMap<String, Object>();
        upperLevels.add(new LevelInfo(ead.getId()));
        String initialFilePath = ead.getPath();
        String eadid = eadContent.getEadid();
        List<ArchivalInstitution> ais = new ArrayList<ArchivalInstitution>();
        while (ai != null) {
            ais.add(ai);
            ai = ai.getParent();
        }
        int depth = 0;
        for (int i = ais.size() - 1; i >= 0; i--) {
            ArchivalInstitution currentAi = ais.get(i);
            String id = SolrValues.AI_PREFIX + currentAi.getAiId();
            String newFacetField = currentAi.getAiname();
            if (currentAi.isGroup()) {
                newFacetField += EadSolrPublisher.COLON + SolrValues.TYPE_GROUP;
            } else {
                newFacetField += EadSolrPublisher.COLON + SolrValues.TYPE_LEAF;
            }
            newFacetField += EadSolrPublisher.COLON + id;
            fullHierarchy.put(SolrFields.AI_DYNAMIC + depth + SolrFields.DYNAMIC_STRING_SUFFIX, newFacetField);
            fullHierarchy.put(SolrFields.AI_DYNAMIC_ID + depth + SolrFields.DYNAMIC_STRING_SUFFIX, id);
            depth++;
        }

        EADCounts eadCounts = new EADCounts();
        EadSolrPublisher solrPublisher = new EadSolrPublisher(ead);
        Class<? extends AbstractContent> clazz = XmlType.getContentType(ead).getEadClazz();
        try {
            EadPublishData publishData = new EadPublishData();
            parse(eadContent, publishData);
            // publishData.setXml(eadContent.getXml());
            publishData.setId(ead.getId().longValue());
            publishData.setUpperLevelUnittitles(upperLevels);
            publishData.setFullHierarchy(fullHierarchy);
            publishData.setArchdesc(true);
            eadCounts.addNumberOfDAOs(solrPublisher.publishArchdesc(publishData));
            Set<String> unitids = new HashSet<String>();
            int cOrderId = 0;
            CLevel clevel = clevelDAO.getTopClevelByFileId(ead.getId(), clazz, cOrderId);
            while (clevel != null) {
                DatabaseXmlCLevelParser.publish(eadCounts, clevel, eadContent.getEcId(), ead,
                        solrPublisher, upperLevels, fullHierarchy, unitids, eadDatabaseSaver);
                cOrderId++;
                clevel = clevelDAO.getTopClevelByFileId(ead.getId(), clazz, cOrderId);
            }
            eadCounts.addClevel(0);
            publishData.setNumberOfDescendents((int) eadCounts.getNumberOfUnits() - 1);
            solrPublisher.publishArchdesc(publishData);
            JpaUtil.beginDatabaseTransaction();
            eadDatabaseSaver.updateAll();
            solrPublisher.commitAll(eadCounts);
            JpaUtil.commitDatabaseTransaction();

        } catch (Exception de) {
            if ((initialFilePath != null) && (initialFilePath.contains(APEnetUtilities.FILESEPARATOR))) {
                LOG.error("Unable to publish ead file to solr: " + de.getMessage(), de);
            }
            JpaUtil.rollbackDatabaseTransaction();
            LOG.error(eadid + ": rollback:", de);
            solrPublisher.rollback();
            throw de;
        }
        return solrPublisher.getSolrTime();
    }

    private static void parse(EadContent eadContent, EadPublishData publishData) throws Exception {
        InputStream inputstream = IOUtils.toInputStream(eadContent.getXml());
        XMLStreamReader xmlReader = getXMLReader(inputstream);
        QName lastElement = null;
        LinkedList<QName> archdescXpathPosition = new LinkedList<QName>();
        LinkedList<QName> fullXpathPosition = new LinkedList<QName>();
        boolean inArchdesc = false;
        EadArchDescCLevelXpathReader archDescParser = new EadArchDescCLevelXpathReader();
        EadGlobalXpathReader fullEadParser = new EadGlobalXpathReader();
        fullEadParser.init();
        archDescParser.init();
        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                lastElement = xmlReader.getName();
                if (ARCHDESC.equals(lastElement)) {
                    inArchdesc = true;
                } else if (inArchdesc) {
                    add(archdescXpathPosition, lastElement);
                    archDescParser.processStartElement(archdescXpathPosition, xmlReader);
                }
                add(fullXpathPosition, lastElement);
                fullEadParser.processStartElement(fullXpathPosition, xmlReader);
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                archDescParser.processEndElement(archdescXpathPosition, xmlReader);
                fullEadParser.processEndElement(fullXpathPosition, xmlReader);
                QName elementName = xmlReader.getName();
                removeLast(archdescXpathPosition, elementName);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                archDescParser.processCharacters(archdescXpathPosition, xmlReader);
                fullEadParser.processEndElement(fullXpathPosition, xmlReader);
            } else if (event == XMLStreamConstants.CDATA) {
                archDescParser.processCharacters(archdescXpathPosition, xmlReader);
                fullEadParser.processEndElement(fullXpathPosition, xmlReader);
            }
        }
        xmlReader.close();
        inputstream.close();
        archDescParser.fillData(publishData, eadContent);
        fullEadParser.fillData(publishData, eadContent);
    }

    private static XMLStreamReader getXMLReader(InputStream inputStream) throws XMLStreamException, IOException {

        XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
        return (XMLStreamReader) inputFactory.createXMLStreamReader(inputStream, ApeXMLConstants.UTF_8);
    }

    private static void add(LinkedList<QName> path, QName qName) {
        // if (!CLEVEL.equals(qName)) {
        path.add(qName);
        // }
    }

    private static void removeLast(LinkedList<QName> path, QName qName) {
        // if (!CLEVEL.equals(qName)) {
        if (!path.isEmpty()) {
            path.removeLast();
        }
        // }

    }
}
