package eu.apenet.dashboard.services.eaccpf.xml.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.eaccpf.publish.EacCpfCounts;
import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfPublishData;
import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfSolrPublisher;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class XmlEacCpfParser extends AbstractParser {

    private static Logger LOG = Logger.getLogger(XmlEacCpfParser.class);
    public static final String UTF_8 = "utf-8";
    public static final QName CPF_RELATION = new QName(APENET_EAC_CPF, "cpfRelation");
    public static final QName RESOURCE_RELATION = new QName(APENET_EAC_CPF, "resourceRelation");
    public static final QName FUNCTION_RELATION = new QName(APENET_EAC_CPF, "functionRelation");

    public static long parseAndPublish(EacCpf eacCpf) throws Exception {
        EacCpfSolrPublisher solrPublisher = new EacCpfSolrPublisher();
        EacCpfCounts eacCpfCounts = new EacCpfCounts();

        FileInputStream fileInputStream = getFileInputStream(eacCpf.getPath());

        XMLStreamReader xmlReader = getXMLReader(fileInputStream);
        QName lastElement = null;

        LinkedList<QName> pathPosition = new LinkedList<QName>();
        EacCpfPublishDataFiller eacCpfParser = new EacCpfPublishDataFiller();
        try {
            JpaUtil.beginDatabaseTransaction();
            for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
                if (event == XMLStreamConstants.START_ELEMENT) {
                    lastElement = xmlReader.getName();
                    add(pathPosition, lastElement);
                    if (CPF_RELATION.equals(lastElement)) {
                        eacCpfCounts.incrementCpfRelations();
                    }
                    if (RESOURCE_RELATION.equals(lastElement)) {
                        eacCpfCounts.incrementResourceRelations();
                    }
                    if (FUNCTION_RELATION.equals(lastElement)) {
                        eacCpfCounts.incrementFunctionRelations();
                    }
                    eacCpfParser.processStartElement(pathPosition, xmlReader);
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    eacCpfParser.processEndElement(pathPosition, xmlReader);
                    QName elementName = xmlReader.getName();
                    removeLast(pathPosition, elementName);
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    eacCpfParser.processCharacters(pathPosition, xmlReader);
                } else if (event == XMLStreamConstants.CDATA) {
                    eacCpfParser.processCharacters(pathPosition, xmlReader);
                }
            }
            xmlReader.close();
            fileInputStream.close();
            EacCpfPublishData publishData = new EacCpfPublishData();
            eacCpfParser.fillData(publishData, eacCpf);
            solrPublisher.publishEacCpf(eacCpf, publishData);
            solrPublisher.commitSolrDocuments();
            eacCpf.setCpfRelations(eacCpfCounts.getNumberOfCpfRelations());
            eacCpf.setResourceRelations(eacCpfCounts.getNumberOfResourceRelations());
            eacCpf.setFunctionRelations(eacCpfCounts.getNumberOfFunctionRelations());
            JpaUtil.commitDatabaseTransaction();
        } catch (Exception de) {
            JpaUtil.rollbackDatabaseTransaction();
            if (solrPublisher != null) {
                LOG.error(eacCpf + ": rollback:", de);
                solrPublisher.unpublish(eacCpf);
            }
            throw de;
        }
        return solrPublisher.getSolrTime();
    }

    private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
        File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
        return new FileInputStream(file);
    }

    private static XMLStreamReader getXMLReader(FileInputStream fileInputStream) throws FileNotFoundException,
            XMLStreamException {
        XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
        return (XMLStreamReader) inputFactory.createXMLStreamReader(fileInputStream, UTF_8);
    }

    protected static class IndexData {

        private long startIndex = -1;
        private long endIndex = -1;
        private long currentIndex = -1;

        public IndexData(long startIndex, long endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;

        }

        public long getCurrentIndex() {
            if (currentIndex == -1) {
                this.currentIndex = startIndex;
            } else if (currentIndex < (endIndex - 1)) {
                currentIndex++;
            }
            return currentIndex;
        }

    }

    private static void add(LinkedList<QName> path, QName qName) {
        //if (!CLEVEL.equals(qName)) {
        path.add(qName);
        //}
    }

    private static void removeLast(LinkedList<QName> path, QName qName) {
        //if (!CLEVEL.equals(qName)) {
        if (!path.isEmpty()) {
            path.removeLast();
        }
        //}

    }
}
