package eu.apenet.dashboard.services.ead.xml.stream.publish;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.solr.SolrValues;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.ApeXmlUtil;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;
import eu.archivesportaleurope.xml.xpath.handler.AttributeXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.CountXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.StringXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.TextXpathHandler;

public class EadArchDescCLevelXpathReader extends AbstractXpathReader<EadPublishData> {
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
	
	private TextXpathHandler controlAccessHandler;
	/*
	 * other
	 */
	private StringXpathHandler didOtherHandler;
	private StringXpathHandler otherHandler;

	private CountXpathHandler countDaoHandler;

	private AttributeXpathHandler langmaterialHander;
	private AttributeXpathHandler daoRoleHandler;
	private AttributeXpathHandler extrefHandler;

	private final static List<String> POSSIBLE_ROLE_DAO_VALUES = Arrays.asList(SolrValues.ROLE_DAOS_ALL);
	
	protected void internalInit() throws Exception {
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
					"unittitle" }, true);
			unittitleHandler.setAllTextBelow(true);
			/*
			 * scopecontent
			 */
			scopecontentHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "scopecontent" }, true);
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
					"not(unittitle | unitid | unitdate | dao)" }, true);
			didOtherHandler.setAllTextBelow(true);
			otherHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "not(scopecontent | did)" }, true);
			otherHandler.setAllTextBelow(true);

			countDaoHandler = new CountXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did", "dao" });
			countDaoHandler.setAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", "thumbnail", true);
			langmaterialHander = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "did",
					"langmaterial", "language" }, "langcode");
			daoRoleHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE,
					new String[] { "did", "dao" }, ApeXMLConstants.XLINK_NAMESPACE, "role");
			daoRoleHandler.setAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", "thumbnail", true);

			extrefHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "otherfindaid",
					"p", "extref" }, ApeXMLConstants.XLINK_NAMESPACE, "href");
			
			controlAccessHandler = new TextXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "controlaccess",
			"subject" });

			getXpathHandlers().add(unitidHandler);
			getXpathHandlers().add(otherUnitidHandler);
			getXpathHandlers().add(unittitleHandler);
			getXpathHandlers().add(scopecontentHandler);
			getXpathHandlers().add(unitdateHandler);
			getXpathHandlers().add(unitdateNormalHander);
			getXpathHandlers().add(didOtherHandler);
			getXpathHandlers().add(otherHandler);
			getXpathHandlers().add(countDaoHandler);
			getXpathHandlers().add(langmaterialHander);
			getXpathHandlers().add(daoRoleHandler);
			getXpathHandlers().add(extrefHandler);
			getXpathHandlers().add(controlAccessHandler);

	}

	public void fillData(EadPublishData publishData, CLevel clevel) {
		//System.out.println("---clevel----");
		fillData(publishData);
		clevel.setUnitid(publishData.getUnitid());
		clevel.setUnittitle(publishData.getFirstUnittitle());
		clevel.setHrefEadid(extrefHandler.getFirstResult());

	}

	public void fillData(EadPublishData publishData, EadContent eadContent) {
			fillData(publishData);
			eadContent.setUnittitle(publishData.getFirstUnittitle());
	}

	public void fillData(EadPublishData publishData) {
		publishData.setUnitid(unitidHandler.getFirstResult());
		publishData.setFirstOtherUnitid(otherUnitidHandler.getFirstResult());
		publishData.setFirstUnittitle(unittitleHandler.getFirstResult());
		publishData.setScopecontent(scopecontentHandler.getResultAsStringWithWhitespace());
		publishData.setUnitdate(unitdateHandler.getFirstResult());
		publishData.setUnitdateNormal(unitdateNormalHander.getFirstResult());
		publishData.setNumberOfDaos(countDaoHandler.getCount());
		publishData.setLangmaterial(langmaterialHander.getResultAsStringWithWhitespace());
		publishData.setControlAccessSubjects(controlAccessHandler.getResultSet());
		for (String roledao : daoRoleHandler.getResult()) {
			String roledaoTemp = roledao.toUpperCase();
			if (POSSIBLE_ROLE_DAO_VALUES.contains(roledaoTemp)){
				publishData.getRoledao().add(roledaoTemp);
			}
		}
		Set<String> otherUnitidSet = new HashSet<String>();
		otherUnitidSet.addAll(unitidHandler.getOtherResultSet());
		otherUnitidSet.addAll(otherUnitidHandler.getResultSet());
		publishData.setOtherUnitid(otherUnitidSet);
		StringBuilder other = new StringBuilder();
		add(other, unittitleHandler.getOtherResultsAsStringWithWhitespace());
		add(other, unitdateHandler.getOtherResultsAsStringWithWhitespace());
		add(other, didOtherHandler.getResultAsStringWithWhitespace());
		add(other, otherHandler.getResultAsStringWithWhitespace());
		publishData.setOtherinfo(other.toString().trim());

	}
	private void add(StringBuilder other, String item){
		if (StringUtils.isNotBlank(item)){
			other.append(ApeXmlUtil.WHITE_SPACE + item);
		}
	}
}
