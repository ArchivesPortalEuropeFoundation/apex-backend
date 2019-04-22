package eu.apenet.dashboard.services.ead.xml.stream;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.services.ead.xml.XMLStreamWriterHolder;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadArchDescCLevelXpathReader;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadPublishData;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadSolrPublisher;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
//import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import gov.loc.ead.C;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlCLevelParser extends AbstractParser {

    public static final QName CLEVEL = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "c");
    private static final QName PERSISTENT_ID = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "id");

    private static JAXBContext clevelContext = null;
    private static Unmarshaller cUnmarshaller = null;

    static {
        try {
            clevelContext = JAXBContext.newInstance(C.class);
            cUnmarshaller = clevelContext.createUnmarshaller();
        } catch (JAXBException e) {
        }
    }

    //private static final Logger LOG = Logger.getLogger(CLevelParser.class);
    public static void parse(EADCounts parentEadCounts, XMLStreamReader xmlReader, Long eadContentId,
            Long parentId, int orderId, Ead ead, EadSolrPublisher solrPublisher, List<LevelInfo> upperLevelUnittitles, Map<String, Object> fullHierarchy, Set<String> unitids)
            throws Exception {
        // QName elementName = xmlReader.getName();
        LinkedList<QName> xpathPosition = new LinkedList<QName>();
        EadPublishData publishData = new EadPublishData();
        CLevel clevel = new CLevel();
        clevel.setEcId(eadContentId);
        clevel.setLeaf(true);
        clevel.setParentId(parentId);
        clevel.setOrderId(orderId);
        int childOrderId = 0;
        //int lastId = ownId;
        List<LevelInfo> unittitles = new ArrayList<LevelInfo>();
        unittitles.addAll(upperLevelUnittitles);
        clevel.setCid(xmlReader.getAttributeValue(null, PERSISTENT_ID.getLocalPart()));
        StringWriter stringWriter = new StringWriter();
        XMLStreamWriterHolder xmlWriterHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance()
                .createXMLStreamWriter(stringWriter));
        xmlWriterHolder.writeCElement(xmlReader);
        boolean foundEndElement = false;
        boolean noCLevelFound = true;
        EADCounts currentEADCounts = new EADCounts();
        Long clId = null;
        QName lastElement = null;
        EadArchDescCLevelXpathReader publishDataFiller = new EadArchDescCLevelXpathReader();
        publishDataFiller.init();
        int event = xmlReader.next();
        while (!foundEndElement && event != XMLStreamConstants.END_DOCUMENT) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                lastElement = xmlReader.getName();
                if (CLEVEL.equals(lastElement)) {
                    if (noCLevelFound) {
                        noCLevelFound = false;
                        xmlWriterHolder.close();
                        clevel.setLeaf(false);
//                        EadPublishData publishData = new EadPublishData();
                        publishDataFiller.fillData(publishData, clevel);
                        if (StringUtils.isNotBlank(clevel.getUnitid())) {
                            if (unitids.contains(clevel.getUnitid())) {
                                clevel.setDuplicateUnitid(true);
                            } else {
                                unitids.add(clevel.getUnitid());
                            }
                        }
                        clevel.setXml(stringWriter.toString());
                        clevel.setcBinary(getBytesFromObjectStr(stringWriter.toString()));
                        JpaUtil.getEntityManager().persist(clevel);
                        stringWriter.close();
                        stringWriter = null;
                        if (clevel.getHrefEadid() != null) {
                            LinkingService.linkWithoutCommit(ead, clevel);
                        }
                        if (solrPublisher != null) {
                            publishData.setId(clevel.getId());
                            publishData.setParentId(parentId);
                            publishData.setLeaf(clevel.isLeaf());
                            publishData.setUpperLevelUnittitles(upperLevelUnittitles);
                            publishData.setFullHierarchy(fullHierarchy);
                            publishData.setDuplicateUnitid(clevel.isDuplicateUnitid());
                            if (publishData.getParentId() == null) {
                                publishData.setOrderId(clevel.getOrderId() + 1);
                            } else {
                                publishData.setOrderId(clevel.getOrderId());
                            }
                            currentEADCounts.addClevel(publishData.getNumberOfDaos());
                            currentEADCounts.setNumberOfTotalDAOs(publishData.getNumberOfDaos());

                            unittitles.add(new LevelInfo(clevel.getId(), clevel.getOrderId(), clevel.getUnittitle()));
                        }
                        clId = clevel.getId();
                        clevel = null;
                    }
//                    parentEadCounts.addEadCounts(currentEADCounts);
                    XmlCLevelParser.parse(currentEADCounts, xmlReader, eadContentId,
                            clId, childOrderId++, ead, solrPublisher, unittitles, fullHierarchy, unitids);

                } else {
                    add(xpathPosition, lastElement);
                    publishDataFiller.processStartElement(xpathPosition, xmlReader);
                    xmlWriterHolder.writeStartElement(xmlReader);
                }

            } else if (event == XMLStreamConstants.END_ELEMENT) {
                publishDataFiller.processEndElement(xpathPosition, xmlReader);
                QName elementName = xmlReader.getName();
                if (CLEVEL.equals(elementName)) {
                    foundEndElement = true;

                } else {
                    removeLast(xpathPosition, elementName);

                }
                xmlWriterHolder.closeElement();

            } else if (event == XMLStreamConstants.CHARACTERS) {
                publishDataFiller.processCharacters(xpathPosition, xmlReader);
                xmlWriterHolder.writeCharacters(xmlReader);
            } else if (event == XMLStreamConstants.CDATA) {
                publishDataFiller.processCharacters(xpathPosition, xmlReader);
                xmlWriterHolder.writeCData(xmlReader);
            }
            if (!foundEndElement) {
                event = xmlReader.next();
            }
        }
        if (noCLevelFound) {
            noCLevelFound = false;
            xmlWriterHolder.close();
//            EadPublishData publishData = new EadPublishData();
            publishDataFiller.fillData(publishData, clevel);
            if (StringUtils.isNotBlank(clevel.getUnitid())) {
                if (unitids.contains(clevel.getUnitid())) {
                    clevel.setDuplicateUnitid(true);
                } else {
                    unitids.add(clevel.getUnitid());
                }
            }
            clevel.setXml(stringWriter.toString());
            clevel.setcBinary(getBytesFromObjectStr(stringWriter.toString()));
            JpaUtil.getEntityManager().persist(clevel);
            stringWriter.close();
            stringWriter = null;
            if (solrPublisher != null) {
                publishData.setId(clevel.getId());
                publishData.setParentId(parentId);
                publishData.setLeaf(clevel.isLeaf());
                publishData.setUpperLevelUnittitles(upperLevelUnittitles);
                publishData.setFullHierarchy(fullHierarchy);
                publishData.setDuplicateUnitid(clevel.isDuplicateUnitid());
                if (publishData.getParentId() == null) {
                    publishData.setOrderId(clevel.getOrderId() + 1);
                } else {
                    publishData.setOrderId(clevel.getOrderId());
                }
                currentEADCounts.addClevel(publishData.getNumberOfDaos());
                if (publishData.getNumberOfDaos() > 0) {
                    currentEADCounts.addNumberOfUnitsWithDaosBelow(1);
                }
//                parentEadCounts.addEadCounts(currentEADCounts);
                clevel.setLeaf(true);
            }
            if (clevel.getHrefEadid() != null) {
                LinkingService.linkWithoutCommit(ead, clevel);
            }
            clId = clevel.getId();
            clevel = null;
        }
        parentEadCounts.addEadCounts(currentEADCounts);
        if (publishData.getId() != null) {
            publishData.setNumberOfDescendents((int) currentEADCounts.getNumberOfUnits() - 1);
            publishData.setNumberOfTotalDaos((int) currentEADCounts.getNumberOfTotalDAOs());
            solrPublisher.publishCLevel(publishData);
        }
    }

    private static void add(LinkedList<QName> path, QName qName) {
        if (!CLEVEL.equals(qName)) {
            path.add(qName);
        }
    }

    private static void removeLast(LinkedList<QName> path, QName qName) {
        if (!CLEVEL.equals(qName)) {
            if (!path.isEmpty()) {
                path.removeLast();
            }
        }

    }

    private static byte[] getBytesFromObjectStr(String objectString) throws JAXBException, IOException {

        InputStream stream = new ByteArrayInputStream(objectString.getBytes());
        C clevelObj = (C) cUnmarshaller.unmarshal(stream);
        stream.close();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput oos = new ObjectOutputStream(baos);
        oos.writeObject(clevelObj);
        oos.flush();
        byte[] clevelObjBytes = baos.toByteArray();
        oos.close();
        return clevelObjBytes;

    }

}
