package eu.apenet.dashboard.services.ead.xml.stream.publish;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.AttributeXpathHandler;
import eu.archivesportaleurope.xml.xpath.CountXpathHandler;
import eu.archivesportaleurope.xml.xpath.StringXpathHandler;
import eu.archivesportaleurope.xml.xpath.TextXpathHandler;
import eu.archivesportaleurope.xml.xpath.XmlStreamHandler;

public class EadPublishDataFiller {
	//private static final Logger LOGGER = Logger.getLogger(EadPublishDataFiller.class);
	/*
	 * unitids
	 */
	private StringXpathHandler unitidHandler;
	private StringXpathHandler otherUnitidHandler;
	/*
	 * unittitle
	 */
	private StringXpathHandler unittitleHandler;
	/*
	 * scopecontent
	 */
	private StringXpathHandler scopecontentHandler;
	/*
	 * unitdate
	 */
	private StringXpathHandler unitdateHandler;
	private AttributeXpathHandler unitdateNormalHander;
	/*
	 * other
	 */
	private StringXpathHandler didOtherHandler;
	private StringXpathHandler otherHandler;

	private CountXpathHandler countDaoHandler;

	private AttributeXpathHandler langmaterialHander;
	private AttributeXpathHandler daoRoleHandler;
	private AttributeXpathHandler extrefHandler;

	private AttributeXpathHandler globalLanguageHandler;
	private List<XmlStreamHandler> archdescClevelHandlers = new ArrayList<XmlStreamHandler>();
	private List<XmlStreamHandler> eadHandlers = new ArrayList<XmlStreamHandler>();
	private boolean archDescCLevelParser = false;

	public EadPublishDataFiller(boolean archDescCLevelParser) {
		this.archDescCLevelParser = archDescCLevelParser;
		if (archDescCLevelParser) {
			/*
			 * unitids
			 */
			unitidHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did", "unitid" });
			unitidHandler.setAllTextBelow(true);
			unitidHandler.setAttribute("type", "call number", false);
			otherUnitidHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "did", "unitid" });
			otherUnitidHandler.setAttribute("type", "call number", true);
			/*
			 * unittitle
			 */
			unittitleHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did",
					"unittitle" });
			unittitleHandler.setAllTextBelow(true);
			/*
			 * scopecontent
			 */
			scopecontentHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "scopecontent" });
			scopecontentHandler.setAllTextBelow(true);

			/*
			 * unitdate
			 */
			unitdateHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "did", "unitdate" });
			unitdateNormalHander = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did",
					"unitdate" }, "normal");
			unitdateNormalHander.setOnlyFirst(true);
			/*
			 * other
			 */
			didOtherHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did",
					"not(unittitle | unitid | unitdate | dao)" });
			didOtherHandler.setAllTextBelow(true);
			otherHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "not(scopecontent | did)" });
			otherHandler.setAllTextBelow(true);

			countDaoHandler = new CountXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did", "dao" });
			countDaoHandler.setAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", "thumbnail", true);
			langmaterialHander = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did",
					"langmaterial", "language" }, "langcode");
			daoRoleHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "did", "dao" }, ApeXMLConstants.XLINK_NAMESPACE, "role");

			extrefHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "otherfindaid",
					"p", "extref" }, ApeXMLConstants.XLINK_NAMESPACE, "href");

			archdescClevelHandlers.add(unitidHandler);
			archdescClevelHandlers.add(otherUnitidHandler);
			archdescClevelHandlers.add(unittitleHandler);
			archdescClevelHandlers.add(scopecontentHandler);
			archdescClevelHandlers.add(unitdateHandler);
			archdescClevelHandlers.add(unitdateNormalHander);
			archdescClevelHandlers.add(didOtherHandler);
			archdescClevelHandlers.add(otherHandler);
			archdescClevelHandlers.add(countDaoHandler);
			archdescClevelHandlers.add(langmaterialHander);
			archdescClevelHandlers.add(daoRoleHandler);
			archdescClevelHandlers.add(extrefHandler);
		} else {
			globalLanguageHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "ead",
					"eadheader", "profiledesc", "langusage", "language" }, "langcode");
			eadHandlers.add(globalLanguageHandler);
		}

	}

	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (archDescCLevelParser) {
			for (XmlStreamHandler handler : archdescClevelHandlers) {
				handler.processCharacters(xpathPosition, xmlReader);
			}
		} else {
			for (XmlStreamHandler handler : eadHandlers) {
				handler.processCharacters(xpathPosition, xmlReader);
			}
		}

	}

	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (archDescCLevelParser) {
			for (XmlStreamHandler handler : archdescClevelHandlers) {
				handler.processStartElement(xpathPosition, xmlReader);
			}
		} else {
			for (XmlStreamHandler handler : eadHandlers) {
				handler.processStartElement(xpathPosition, xmlReader);
			}
		}

	}

	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (archDescCLevelParser) {
			for (XmlStreamHandler handler : archdescClevelHandlers) {
				handler.processEndElement(xpathPosition, xmlReader);
			}
		} else {
			for (XmlStreamHandler handler : eadHandlers) {
				handler.processEndElement(xpathPosition, xmlReader);
			}
		}
	}

	public void fillData(EadPublishData publishData, CLevel clevel) {
		//System.out.println("---clevel----");
		fillData(publishData);
		clevel.setUnitid(publishData.getUnitid());
		clevel.setUnittitle(publishData.getFirstUnittitle());
		clevel.setHrefEadid(extrefHandler.getFirstResult());
		publishData.setLevel(clevel.getLevel());

	}

	public void fillData(EadPublishData publishData, EadContent eadContent) {
		if (archDescCLevelParser) {
			//System.out.println("---archdesc----");
			fillData(publishData);
			eadContent.setUnittitle(publishData.getFirstUnittitle());

			// eadContent.setUnitid(publishData.getUnitid());
		} else {
			publishData.setGlobalLanguage(globalLanguageHandler.getResultAsStringWithWhitespace());
		}
	}

	public void fillData(EadPublishData publishData) {
		publishData.setUnitid(unitidHandler.getFirstResult());
		publishData.setFirstOtherUnitid(otherUnitidHandler.getFirstResult());
		publishData.setOtherUnitid(otherUnitidHandler.getResultAsStringWithWhitespace());
		publishData.setFirstUnittitle(unittitleHandler.getFirstResult());
		publishData.setScopecontent(scopecontentHandler.getResultAsStringWithWhitespace());
		publishData.setUnitdate(unitdateHandler.getFirstResult());
		publishData.setUnitdateNormal(unitdateNormalHander.getFirstResult());
		publishData.setNumberOfDaos(countDaoHandler.getCount());
		publishData.setLangmaterial(langmaterialHander.getResultAsStringWithWhitespace());
		for (String roledao : daoRoleHandler.getResult()) {
			publishData.getRoledao().add(roledao);
		}
		StringBuilder otherUnitid = new StringBuilder();
		add(otherUnitid, unitidHandler.getOtherResultsAsStringWithWhitespace());
		add(otherUnitid, otherUnitidHandler.getResultAsStringWithWhitespace());
		publishData.setOtherUnitid(otherUnitid.toString().trim());
		StringBuilder other = new StringBuilder();
		add(other, unittitleHandler.getOtherResultsAsStringWithWhitespace());
		add(other, unitdateHandler.getOtherResultsAsStringWithWhitespace());
		add(other, didOtherHandler.getResultAsStringWithWhitespace());
		add(other, otherHandler.getResultAsStringWithWhitespace());
		publishData.setOtherinfo(other.toString().trim());

	}
	private void add(StringBuilder other, String item){
		if (StringUtils.isNotBlank(item)){
			other.append(StringXpathHandler.WHITE_SPACE + item);
		}
	}
}
