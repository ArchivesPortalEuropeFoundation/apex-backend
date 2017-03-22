package eu.apenet.dashboard.services.ead.xml.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.apache.commons.lang.StringUtils;

import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.dashboard.services.ead.database.EadDatabaseSaver;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadArchDescCLevelXpathReader;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadPublishData;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadSolrPublisher;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.xml.ApeXMLConstants;

public class DatabaseXmlCLevelParser {

    private static final int NUMBER_OF_CLEVEL_ONCE = 1;

    public static EADCounts publish(EADCounts parentEADCounts, CLevel clevel, Long eadContentId,
            Ead ead, EadSolrPublisher solrPublisher, List<LevelInfo> upperLevelUnittitles, Map<String, Object> fullHierarchy, Set<String> unitids, EadDatabaseSaver eadDatabaseSaver)
            throws Exception {
        CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
        List<LevelInfo> unittitles = new ArrayList<LevelInfo>();
        unittitles.addAll(upperLevelUnittitles);
        EADCounts currentEadCounts = new EADCounts();
        EadPublishData publishData = new EadPublishData();
        parse(clevel, publishData);
        publishData.setId(clevel.getId());
        publishData.setParentId(clevel.getParentClId());
        publishData.setLeaf(clevel.isLeaf());
        publishData.setUpperLevelUnittitles(upperLevelUnittitles);
        publishData.setFullHierarchy(fullHierarchy);
        if (StringUtils.isNotBlank(clevel.getUnitid())) {
            if (unitids.contains(clevel.getUnitid())) {
                publishData.setDuplicateUnitid(true);
                clevel.setDuplicateUnitid(true);
                eadDatabaseSaver.update(clevel);
            } else {
                unitids.add(clevel.getUnitid());
            }
        }

        if (publishData.getParentId() == null) {
            publishData.setOrderId(clevel.getOrderId() + 1);
        } else {
            publishData.setOrderId(clevel.getOrderId());
        }
        currentEadCounts.addClevel(publishData.getNumberOfDaos());
        currentEadCounts.setNumberOfUnitsWithDaosBelow(publishData.getNumberOfDaos());
        if ((ead instanceof SourceGuide || ead instanceof HoldingsGuide) && clevel.getHrefEadid() != null) {
            eadDatabaseSaver.insert(LinkingService.getNewLink(ead, clevel));
        }
        unittitles.add(new LevelInfo(clevel.getId(), clevel.getOrderId(), clevel.getUnittitle()));
        int childOrderId = 0;
        List<CLevel> clevels = clevelDAO.findChildCLevels(clevel.getId(), childOrderId, NUMBER_OF_CLEVEL_ONCE);
        while (clevels.size() > 0) {
            DatabaseXmlCLevelParser.publish(currentEadCounts, clevels.get(0), eadContentId, ead, solrPublisher, unittitles, fullHierarchy, unitids, eadDatabaseSaver);
            clevels.remove(0);
            childOrderId++;
            if (clevels.size() == 0) {
                clevels = clevelDAO.findChildCLevels(clevel.getId(), childOrderId, NUMBER_OF_CLEVEL_ONCE);
            }
        }
        parentEADCounts.addEadCounts(currentEadCounts);
        publishData.setNumberOfDescendents((int) currentEadCounts.getNumberOfUnits() - 1);
        solrPublisher.publishCLevel(publishData);
        return currentEadCounts;

    }

    private static void parse(CLevel clevel, EadPublishData publishData) throws Exception {
        InputStream inputstream = IOUtils.toInputStream(clevel.getXml());
        XMLStreamReader xmlReader = getXMLReader(inputstream);
        QName lastElement = null;
        LinkedList<QName> xpathPosition = new LinkedList<QName>();
        EadArchDescCLevelXpathReader clevelParser = new EadArchDescCLevelXpathReader();
        clevelParser.init();
        for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                lastElement = xmlReader.getName();
                if (!XmlCLevelParser.CLEVEL.equals(lastElement)) {
                    add(xpathPosition, lastElement);
                    clevelParser.processStartElement(xpathPosition, xmlReader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                clevelParser.processEndElement(xpathPosition, xmlReader);
                QName elementName = xmlReader.getName();
                removeLast(xpathPosition, elementName);
            } else if (event == XMLStreamConstants.CHARACTERS) {
                clevelParser.processCharacters(xpathPosition, xmlReader);
            } else if (event == XMLStreamConstants.CDATA) {
                clevelParser.processCharacters(xpathPosition, xmlReader);
            }
        }
        xmlReader.close();
        inputstream.close();
        clevelParser.fillData(publishData, clevel);
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
