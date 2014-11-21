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
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadPublishData;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadPublishDataFiller;
import eu.apenet.dashboard.services.ead.xml.stream.publish.EadSolrPublisher;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
//import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class XmlCLevelParser extends AbstractParser {

	public static final QName CLEVEL = new QName(APENET_EAD, "c");
	private static final QName PERSISTENT_ID = new QName(APENET_EAD, "id");

	//private static final Logger LOG = Logger.getLogger(CLevelParser.class);


	public static EADCounts parse(XMLStreamReader xmlReader, Long eadContentId,
			Long parentId, int orderId, Ead ead, EadSolrPublisher solrPublisher, List<LevelInfo> upperLevelUnittitles, Map<String, Object> fullHierarchy, Set<String> unitids)
			throws Exception {
		// QName elementName = xmlReader.getName();
		LinkedList<QName> xpathPosition = new LinkedList<QName>();
		CLevel clevel = new CLevel();
		clevel.setEcId(eadContentId);
		clevel.setLeaf(true);
		clevel.setParentClId(parentId);
		clevel.setOrderId(orderId);
		int childOrderId = 0;
		//int lastId = ownId;
		List<LevelInfo> unittitles = new ArrayList<LevelInfo>();
		unittitles.addAll(upperLevelUnittitles);
		clevel.setPersistentId(xmlReader.getAttributeValue(null, PERSISTENT_ID.getLocalPart()));
		StringWriter stringWriter = new StringWriter();
		XMLStreamWriterHolder xmlWriterHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance()
				.createXMLStreamWriter(stringWriter));
		xmlWriterHolder.writeCElement(xmlReader);
		boolean foundEndElement = false;
		boolean noCLevelFound = true;	
		EADCounts eadCounts = new EADCounts();
		Long clId = null;
		QName lastElement = null;
		EadPublishDataFiller publishDataFiller = new EadPublishDataFiller(true);
		int event =  xmlReader.next();
		while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlReader.getName();
				if (CLEVEL.equals(lastElement)) {
					if (noCLevelFound){
						noCLevelFound = false;
						xmlWriterHolder.close();
						clevel.setLeaf(false);
						EadPublishData publishData = new EadPublishData();
						publishDataFiller.fillData(publishData, clevel);	
						if (StringUtils.isNotBlank(clevel.getUnitid())){
							if (unitids.contains(clevel.getUnitid())){
								clevel.setDuplicateUnitid(true);
							}else {
								unitids.add(clevel.getUnitid());
							}
						}			
						clevel.setXml(stringWriter.toString());
						JpaUtil.getEntityManager().persist(clevel);
						stringWriter.close();
						stringWriter = null;
						if (clevel.getHrefEadid() != null){
							LinkingService.linkWithoutCommit(ead, clevel);
						}
						if (solrPublisher != null) {
							publishData.setId(clevel.getClId());
							publishData.setParentId(parentId);
							publishData.setLeaf(clevel.isLeaf());
							publishData.setUpperLevelUnittitles(upperLevelUnittitles);
							publishData.setFullHierarchy(fullHierarchy);
							publishData.setDuplicateUnitid(clevel.isDuplicateUnitid());
							if (publishData.getParentId() == null) {
								publishData.setOrderId(clevel.getOrderId()+1);
							}else {
								publishData.setOrderId(clevel.getOrderId());
							}
							eadCounts.addClevel(solrPublisher.publishCLevel(publishData));	
							unittitles.add(new LevelInfo(clevel.getClId(),clevel.getOrderId(), clevel.getUnittitle()));	
						}						
						clId  = clevel.getClId();
						clevel = null;
					}					
					eadCounts.addEadCounts(XmlCLevelParser.parse(xmlReader, eadContentId,
							clId, childOrderId++,ead, solrPublisher, unittitles, fullHierarchy, unitids));

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
			if (!foundEndElement){
				event = xmlReader.next();
			}
		}
		if (noCLevelFound){
			noCLevelFound = false;
			xmlWriterHolder.close();
			EadPublishData publishData = new EadPublishData();
			publishDataFiller.fillData(publishData, clevel);						
			if (StringUtils.isNotBlank(clevel.getUnitid())){
				if (unitids.contains(clevel.getUnitid())){
					clevel.setDuplicateUnitid(true);
				}else {
					unitids.add(clevel.getUnitid());
				}
			}

			clevel.setXml(stringWriter.toString());
			JpaUtil.getEntityManager().persist(clevel);
			stringWriter.close();
			stringWriter = null;
			if (solrPublisher != null) {
				publishData.setId(clevel.getClId());
				publishData.setParentId(parentId);
				publishData.setLeaf(clevel.isLeaf());
				publishData.setUpperLevelUnittitles(upperLevelUnittitles);
				publishData.setFullHierarchy(fullHierarchy);
				publishData.setDuplicateUnitid(clevel.isDuplicateUnitid());
				if (publishData.getParentId() == null) {
					publishData.setOrderId(clevel.getOrderId()+1);
				}else {
					publishData.setOrderId(clevel.getOrderId());
				}
				eadCounts.addClevel(solrPublisher.publishCLevel(publishData));
				clevel.setLeaf(true);
			}						
			if (clevel.getHrefEadid() != null){
				LinkingService.linkWithoutCommit(ead, clevel);
			}
			clId  = clevel.getClId();
			clevel = null;
		}	
		return eadCounts;

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


}
