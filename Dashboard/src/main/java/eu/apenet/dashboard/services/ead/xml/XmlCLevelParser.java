package eu.apenet.dashboard.services.ead.xml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.publish.PublishData;
import eu.apenet.dashboard.services.ead.publish.SolrPublisher;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class XmlCLevelParser extends AbstractParser {
	public static final QName CLEVEL = new QName(APENET_EAD, "c");
	private static final QName UNITTITLE = new QName(APENET_EAD, "unittitle");
	private static final QName UNITID = new QName(APENET_EAD, "unitid");
	private static final QName LEVEL = new QName(APENET_EAD, "level");
	public static final QName OTHER_FINDINGAID = new QName(APENET_EAD,
			"otherfindaid");
	public static final QName P = new QName(APENET_EAD, "p");
	public static final QName EXREF = new QName(APENET_EAD, "extref");
	public static final QName HREF = new QName(XLINK, "href");
	//private static final Logger LOG = Logger.getLogger(CLevelParser.class);


	public static EADCounts parse(XMLStreamReader xmlReader, Long eadContentId,
			Long parentId, int orderId, Ead ead, SolrPublisher solrPublisher, List<LevelInfo> upperLevelUnittitles, Map<String, Object> fullHierarchy)
			throws Exception {
		// QName elementName = xmlReader.getName();
		LinkedList<QName> path = new LinkedList<QName>();
		CLevel clevel = new CLevel();
		clevel.setEcId(eadContentId);
		clevel.setLeaf(true);
		clevel.setParentClId(parentId);
		clevel.setOrderId(orderId);
		int childOrderId = 0;
		//int lastId = ownId;
		List<LevelInfo> unittitles = new ArrayList<LevelInfo>();
		unittitles.addAll(upperLevelUnittitles);
		clevel.setLevel(xmlReader.getAttributeValue(null, LEVEL.getLocalPart()));
		StringWriter stringWriter = new StringWriter();
		XMLStreamWriterHolder xmlWriterHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance()
				.createXMLStreamWriter(stringWriter));
		xmlWriterHolder.writeCElement(xmlReader);
		boolean foundEndElement = false;
		boolean noCLevelFound = true;	
		EADCounts eadCounts = new EADCounts();
		Long clId = null;
		QName lastElement = null;
		int event =  xmlReader.next();
		while(!foundEndElement && event != XMLStreamConstants.END_DOCUMENT){
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElement = xmlReader.getName();
				if (CLEVEL.equals(lastElement)) {
					if (noCLevelFound){
						noCLevelFound = false;
						xmlWriterHolder.close();
						clevel.setLeaf(false);
						clevel.setXml(stringWriter.toString());
						JpaUtil.getEntityManager().persist(clevel);
						stringWriter.close();
						stringWriter = null;
						if (clevel.getHrefEadid() != null){
							LinkingService.linkWithoutCommit(ead, clevel);
						}
						if (solrPublisher != null) {
							PublishData publishData = new PublishData();
							publishData.setXml(clevel.getXml());
							publishData.setId(clevel.getClId());
							publishData.setParentId(parentId);
							publishData.setLeaf(clevel.isLeaf());
							publishData.setUpperLevelUnittitles(upperLevelUnittitles);
							publishData.setFullHierarchy(fullHierarchy);
							if (publishData.getParentId() == null) {
								publishData.setOrderId(clevel.getOrderId()+1);
							}else {
								publishData.setOrderId(clevel.getOrderId());
							}
							eadCounts.addClevel(solrPublisher.parseCLevel(publishData));	
							unittitles.add(new LevelInfo(clevel.getClId(),clevel.getOrderId(), clevel.getUnittitle()));	
						}						
						clId  = clevel.getClId();
						clevel = null;
					}					
					eadCounts.addEadCounts(XmlCLevelParser.parse(xmlReader, eadContentId,
							clId, childOrderId++,ead, solrPublisher, unittitles, fullHierarchy));

				} else {
					add(path, lastElement);
					if (EXREF.equals(lastElement)) {
						boolean match = path.size() == 3
								&& OTHER_FINDINGAID.equals(path.get(0))
								&& P.equals(path.get(1))
								&& EXREF.equals(path.get(2));
						if (match) {
							clevel.setHrefEadid(xmlReader.getAttributeValue(
									HREF.getNamespaceURI(), HREF.getLocalPart()));
						}
					}
					xmlWriterHolder.writeStartElement(xmlReader);
				}

			} else if (event == XMLStreamConstants.END_ELEMENT) {
				QName elementName = xmlReader.getName();
				if (CLEVEL.equals(elementName)) {
					foundEndElement = true;

				} else {
					removeLast(path, elementName);
					
				}
				xmlWriterHolder.closeElement();

			} else if (event == XMLStreamConstants.CHARACTERS) {

				if (UNITTITLE.equals(lastElement)) {
					if (clevel.getUnittitle() == null) {
						clevel.setUnittitle(removeUnusedCharacters(xmlReader.getText()));
					}
					lastElement = null;

				} else if (UNITID.equals(lastElement)) {
					if (clevel.getUnitid() == null) {
						clevel.setUnitid(removeUnusedCharacters(xmlReader.getText()));
					}
					lastElement = null;

				}
				xmlWriterHolder.writeCharacters(xmlReader);
			} else if (event == XMLStreamConstants.CDATA) {
				if (UNITTITLE.equals(lastElement)) {
					if (clevel.getUnittitle() == null) {
						clevel.setUnittitle(removeUnusedCharacters(xmlReader.getText()));
					}
					lastElement = null;

				} else if (UNITID.equals(lastElement)) {
					if (clevel.getUnitid() == null) {
						clevel.setUnitid(removeUnusedCharacters(xmlReader.getText()));
					}
					lastElement = null;

				}
				xmlWriterHolder.writeCData(xmlReader);
			}
			if (!foundEndElement){
				event = xmlReader.next();
			}
		}
		if (noCLevelFound){
			noCLevelFound = false;
			xmlWriterHolder.close();
			clevel.setXml(stringWriter.toString());
			JpaUtil.getEntityManager().persist(clevel);
			stringWriter.close();
			stringWriter = null;
			if (solrPublisher != null) {
				PublishData publishData = new PublishData();
				publishData.setXml(clevel.getXml());
				publishData.setId(clevel.getClId());
				publishData.setParentId(parentId);
				publishData.setLeaf(clevel.isLeaf());
				publishData.setUpperLevelUnittitles(upperLevelUnittitles);
				publishData.setFullHierarchy(fullHierarchy);
				if (publishData.getParentId() == null) {
					publishData.setOrderId(clevel.getOrderId()+1);
				}else {
					publishData.setOrderId(clevel.getOrderId());
				}
				eadCounts.addClevel(solrPublisher.parseCLevel(publishData));
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
