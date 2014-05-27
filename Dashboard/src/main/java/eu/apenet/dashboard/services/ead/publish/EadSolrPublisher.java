package eu.apenet.dashboard.services.ead.publish;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import eu.apenet.commons.solr.AbstractSolrServerHolder;
import eu.apenet.commons.solr.EadSolrServerHolder;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.dashboard.services.ead.xml.EADNamespaceContext;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.Warnings;

public class EadSolrPublisher extends AbstractSolrPublisher{

	private static final String LEVEL_CLEVEL = "clevel";
	private static final String LEVEL_ARCHDESC = "archdesc";
	private static final Logger LOG = Logger.getLogger(EadSolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");

	private String language;
	private String archdesc_langmaterial;
	private String eadidstring;
	private String fond;
	private String unitidfond;
	private EadDAO eadDao;
	private Ead ead;
	private XmlType xmlType;
	private ArchivalInstitution archivalinstitution;
	private Boolean existunitid_archdesc = false;

	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
	private static XPathExpression eadidExpression;
	private static XPathExpression eadidIdentifierExpression;
	private static XPathExpression textBelowExpression;
	private static XPathExpression languageExpression;
	private static XPathExpression archdescLangmaterialExpression;
	private static XPathExpression archdescUnitidExpression;
	private static XPathExpression fondTitleExpression;
	private static XPathExpression countNumberOfDAOsExpression;
	private static XPathExpression daoRoleAttributeExpression;
	private static XPathExpression cLevelExpression;	
	private static XPathExpression archdescExpression;	
	private static XPathExpression levelAttributeExpression;
	private static XPathExpression didExpression;
	private static XPathExpression unitidExpression;
	private static XPathExpression otherUnitidExpression;
	private static XPathExpression unittitleExistExpression;
	private static XPathExpression unittitleExpression;
	private static XPathExpression otherUnittitleExpression;
	private static XPathExpression unitdateExpression;
	private static XPathExpression otherUnitdateExpression;
	private static XPathExpression unitdateNormalExpression;
	private static XPathExpression langmaterialExpression;
	private static XPathExpression didOtherExpression;
	private static XPathExpression scopecontentExpression;
	private static XPathExpression otherExpression;
	private static XPathExpression displayIntroExpression;
	private static XPathExpression displayDidExpression;
	private static DocumentBuilder builder;
	static {
		try {
			XPATH.setNamespaceContext(new EADNamespaceContext());
			eadidExpression = XPATH.compile("/ead:ead/ead:eadheader/ead:eadid");
			eadidIdentifierExpression = XPATH.compile("@identifier");
			languageExpression = XPATH
					.compile("/ead:ead/ead:eadheader/ead:profiledesc/ead:langusage/ead:language/@langcode");
			archdescExpression = XPATH.compile("/ead:ead/ead:archdesc");
			cLevelExpression = XPATH.compile("/ead:c");
			archdescLangmaterialExpression = XPATH
					.compile("/ead:ead/ead:archdesc/ead:did/ead:langmaterial/ead:language/@langcode");
			archdescUnitidExpression = XPATH.compile("/ead:ead/ead:archdesc/ead:did/ead:unitid/text()");
			fondTitleExpression = XPATH
					.compile("/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper[1]//text()");
			textBelowExpression = XPATH.compile("./text()");
			countNumberOfDAOsExpression = XPATH.compile("count(./ead:dao[not(@xlink:title='thumbnail')])");
			daoRoleAttributeExpression = XPATH.compile("./ead:dao/@xlink:role");
			// daoRoleAttributeExpression =
			// XPATH.compile("./ead:dao[not(@xlink:title='thumbnail')]/@xlink:role");
			levelAttributeExpression = XPATH.compile("./@level");
			didExpression = XPATH.compile("./ead:did");
			unitidExpression = XPATH.compile("./ead:unitid[@type='call number']/text()");
			otherUnitidExpression = XPATH
					.compile("./ead:unitid[@type='former call number' or @type='file reference']/text()");
			unittitleExistExpression = XPATH.compile("./ead:unittitle");
			unittitleExpression = XPATH.compile("./ead:unittitle[1]//text()");
			otherUnittitleExpression = XPATH.compile("./ead:unittitle[position() > 1]//text()");
			unitdateExpression = XPATH.compile("./ead:unitdate/text()");
			otherUnitdateExpression = XPATH.compile("./ead:unitdate[position() > 1]//text()");
			unitdateNormalExpression = XPATH.compile("./ead:unitdate/@normal");
			langmaterialExpression = XPATH.compile("./ead:langmaterial/ead:language/@langcode");
			didOtherExpression = XPATH
					.compile("./node()[not(name()='unittitle' or name()='unitid' or name()='unitdate' or name()='dao')]//text()");
			scopecontentExpression = XPATH.compile("./ead:scopecontent//text()");
			otherExpression = XPATH.compile("./node()[not(name()='scopecontent' or name()='did')]//text()");
			displayIntroExpression = XPATH.compile("/ead:ead/ead:archdesc/ead:scopecontent or"
					+ "/ead:ead/ead:archdesc/ead:bioghist or " + "/ead:ead/ead:archdesc/ead:custodhist or "
					+ "/ead:ead/ead:archdesc/ead:appraisal or " + "/ead:ead/ead:archdesc/ead:processinfo or "
					+ "/ead:ead/ead:archdesc/ead:accruals or " + "/ead:ead/ead:archdesc/ead:arrangement or "
					+ "/ead:ead/ead:archdesc/ead:fileplan or " + "/ead:ead/ead:archdesc/ead:accessrestrict or "
					+ "/ead:ead/ead:archdesc/ead:userestrict or " + "/ead:ead/ead:archdesc/ead:phystech or "
					+ "/ead:ead/ead:archdesc/ead:otherfindaid or " + "/ead:ead/ead:archdesc/ead:relatedmaterial or "
					+ "/ead:ead/ead:archdesc/ead:separatedmaterial or " + "/ead:ead/ead:archdesc/ead:altformavail or "
					+ "/ead:ead/ead:archdesc/ead:originalsloc or " + "/ead:ead/ead:archdesc/ead:bibliography or "
					+ "/ead:ead/ead:archdesc/ead:prefercite or " + "/ead:ead/ead:archdesc/ead:odd or "
					+ "/ead:ead/ead:archdesc/ead:controlaccess or " + "/ead:ead/ead:archdesc/ead:did/ead:note");
			displayDidExpression = XPATH.compile("/ead:ead/ead:archdesc/ead:did/ead:unitid or"
					+ "/ead:ead/ead:archdesc/ead:did/ead:unittitle or "
					+ "/ead:ead/ead:archdesc/ead:did/ead:langmaterial or "
					+ "/ead:ead/ead:archdesc/ead:did/ead:origination or "
					+ "/ead:ead/ead:archdesc/ead:did/ead:repository or "
					+ "/ead:ead/ead:archdesc/ead:did/ead:physloc/@label or "
					+ "/ead:ead/ead:archdesc/ead:did/ead:materialspec or "
					+ "/ead:ead/ead:archdesc/ead:did/ead:physdesc or " + "/ead:ead/ead:archdesc/ead:did/ead:dao");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setIgnoringComments(true);  
			builder = factory.newDocumentBuilder();
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
		}
	}

	public EadSolrPublisher(Ead ead) {
		this.ead = ead;
		xmlType = XmlType.getContentType(ead);
		eadDao = DAOFactory.instance().getEadDAO();
	}

	public long parseHeader(EadContent eadContent, PublishData publishData) throws Exception {
		long numberOfDaos = 0l;
		archivalinstitution = ead.getArchivalInstitution();
		StringReader stringReader = new StringReader(eadContent.getXml());
		Document doc = builder.parse(new InputSource(stringReader));
		stringReader.close();
		doc.getDocumentElement().normalize();
		// Obtain the archival institution.

		numberOfDaos = extractGeneralData(doc, eadContent);
		if (eadContent.isDisplayDid() || eadContent.isDisplayIntro()){
			Node archdescNode = (Node) archdescExpression.evaluate(doc, XPathConstants.NODE);
			parseCLevelOrArchdesc(archdescNode,publishData);
		}
		return numberOfDaos;
	}

	private long extractGeneralData(Document doc, EadContent eadContent) throws XPathExpressionException {
		Boolean displayIntro = (Boolean) displayIntroExpression.evaluate(doc, XPathConstants.BOOLEAN);
		eadContent.setDisplayIntro(displayIntro);
		Boolean displayDid = (Boolean) displayDidExpression.evaluate(doc, XPathConstants.BOOLEAN);
		eadContent.setDisplayDid(displayDid);
		Node eadidNode = (Node) eadidExpression.evaluate(doc, XPathConstants.NODE);
		eadidstring = (String) textBelowExpression.evaluate(eadidNode, XPathConstants.STRING);
		if (StringUtils.isBlank(eadidstring)) {
			eadidstring = (String) eadidIdentifierExpression.evaluate(eadidNode, XPathConstants.STRING);
		}
		eadidstring = removeUnusedCharacters(eadidstring);
		// Get out the language. @LANGCODE of all <language> node of
		// <langusage>.
		NodeList languageNodeList = (NodeList) languageExpression.evaluate(doc, XPathConstants.NODESET);
		language = getText(languageNodeList);
		// Obtain the titleproper of the file to describe the fond.
		NodeList fondNodeList = (NodeList) fondTitleExpression.evaluate(doc, XPathConstants.NODESET);
		fond = getText(fondNodeList);
		// Obtain unittile of the archdesc to fill the fond.
		// and the unitid of the archdesc to fill de unitidfond.
		// Now I obtain the langmaterial of the archdesc to add to the index.
		NodeList langmaterialNodeList = (NodeList) archdescLangmaterialExpression.evaluate(doc, XPathConstants.NODESET);
		archdesc_langmaterial = getText(langmaterialNodeList);
		unitidfond = removeUnusedCharacters((String) archdescUnitidExpression.evaluate(doc, XPathConstants.STRING));
		if (StringUtils.isNotBlank(unitidfond)) {
			existunitid_archdesc = true;
		}

		// count number of daos
		Double numberOfDaos = (Double) countNumberOfDAOsExpression.evaluate(doc, XPathConstants.NUMBER);
		return numberOfDaos.longValue();
	}

	public long parseCLevel(PublishData indexData) throws Exception {
		StringReader stringReader = new StringReader(indexData.getXml());
		Document doc = builder.parse(new InputSource(stringReader));
		stringReader.close();
		doc.getDocumentElement().normalize();
		Node cLevelNode = (Node) cLevelExpression.evaluate(doc, XPathConstants.NODE);
		return parseCLevelOrArchdesc(cLevelNode, indexData);
	}

	private long parseCLevelOrArchdesc(Node cLevelOrArchdescNode, PublishData publishData) throws Exception {
		String startdate = null;
		String enddate = null;
		String alterdate = "";
		String attributeLevel = (String) levelAttributeExpression.evaluate(cLevelOrArchdescNode, XPathConstants.STRING);
		// did parsing
		Node didNode = (Node) didExpression.evaluate(cLevelOrArchdescNode, XPathConstants.NODE);
		String unitid = removeUnusedCharacters((String) unitidExpression.evaluate(didNode, XPathConstants.STRING));
		String otherunitid = getText((NodeList) otherUnitidExpression.evaluate(didNode, XPathConstants.NODESET));

		String unitdatenormal = (String) unitdateNormalExpression.evaluate(didNode, XPathConstants.STRING);
		Double numberOfDaos = (Double) countNumberOfDAOsExpression.evaluate(didNode, XPathConstants.NUMBER);
		boolean hasDao = numberOfDaos != null && numberOfDaos > 0;
		// String roleDao = (String)
		// daoRoleAttributeExpression.evaluate(didNode, XPathConstants.STRING);
		Set<String> roleDao = getTextsWithoutMultiplity((NodeList) daoRoleAttributeExpression.evaluate(didNode,
				XPathConstants.NODESET));
		String clevelLangmaterial = getText((NodeList) langmaterialExpression
				.evaluate(didNode, XPathConstants.NODESET));
		String otherinfo = "";
		String unittitle = "";
		boolean hasUnittitle = (Boolean) unittitleExistExpression.evaluate(didNode, XPathConstants.BOOLEAN);
		if (hasUnittitle) {
			unittitle = getText((NodeList) unittitleExpression.evaluate(didNode, XPathConstants.NODESET));
			otherinfo += WHITE_SPACE
					+ getText((NodeList) otherUnittitleExpression.evaluate(didNode, XPathConstants.NODESET));
		}
		String unitdate = removeUnusedCharacters((String) unitdateExpression.evaluate(didNode, XPathConstants.STRING));
		if (StringUtils.isNotBlank(unitdate)) {
			otherinfo += WHITE_SPACE
					+ getText((NodeList) otherUnitdateExpression.evaluate(didNode, XPathConstants.NODESET));
		}
		otherinfo += WHITE_SPACE + getText((NodeList) didOtherExpression.evaluate(didNode, XPathConstants.NODESET));
		// other parsing
		String scopecontent = getText((NodeList) scopecontentExpression.evaluate(cLevelOrArchdescNode, XPathConstants.NODESET));
		otherinfo += WHITE_SPACE + getText((NodeList) otherExpression.evaluate(cLevelOrArchdescNode, XPathConstants.NODESET));

		if (StringUtils.isNotBlank(unitid) && (attributeLevel == "fonds") && (existunitid_archdesc == false)) {
			unitidfond = unitid;
		}
		if (StringUtils.isNotBlank(unitdate)) {
			if (StringUtils.isNotBlank(unitdatenormal)) {
				try {
					if (unitdatenormal.contains("/")) {
						String[] list = unitdatenormal.split("/");
						String sd = "";
						String ed = "";
						String od = "";
						if (list.length == 2) {// The meaning is:
												// startdate/enddate
							sd = list[0].trim();
							startdate = obtainDate(sd, true);
							ed = list[1].trim();
							if ((sd.contains("-")) && (sd.equals(ed))) {
								enddate = obtainDate(ed, true);
							} else {
								enddate = obtainDate(ed, false);
							}
							alterdate = unitdate;
						} else { // There is only one date.
							od = list[0].trim();
							startdate = obtainDate(od, true);
							enddate = obtainDate(od, false);
							alterdate = unitdate;
						}
					} else {// if the date is a year.
						startdate = obtainDate(unitdatenormal, true);
						enddate = obtainDate(unitdatenormal, false);
						alterdate = unitdate;
					}
				} catch (Exception ex) {
					LOG.info("Warning: The date is not correct" + ex.getMessage());
					alterdate = unitdate;
					if (alterdate.contains("\n")) {
						String[] list = alterdate.split("\n");
						String alterdate2 = "";
						for (int x = 0; x < list.length; x++) {
							alterdate2 = alterdate2 + WHITE_SPACE + list[x].trim();
						}
						unitdate = null;
						alterdate = alterdate2;
					}
				}
			} else {
				alterdate = unitdate;
			}
		}

		// if c-level contains langmaterial Node this will be indexed
		// but if this c-level doesn't contain langmaterial Node, then
		// langmaterial content of archdesc Node will be indexed.
		String langmaterial = "";
		if (StringUtils.isNotBlank(clevelLangmaterial)) {
			langmaterial = clevelLangmaterial;
		} else {
			langmaterial = archdesc_langmaterial;
		}

		publishNode(publishData, unitid, otherunitid, scopecontent, unittitle, startdate, enddate,
				alterdate, hasDao, roleDao, otherinfo, langmaterial);
		return numberOfDaos.longValue();
	}

	private void removeWarnings() {
		WarningsDAO warningsDao = DAOFactory.instance().getWarningsDAO();
		Set<Warnings> warningsSet = ead.getWarningses();
		for (Warnings warnings : warningsSet) {
			warningsDao.deleteSimple(warnings);
		}
	}


	private void publishNode(PublishData publishData, String unitid, String otherunitid, String scopecontent, String title,
			 String sdate, String edate, String alterdate, boolean dao, Set<String> roleDao,
			String otherinfo, String langmaterial) throws MalformedURLException, SolrServerException, IOException {
		String solrPrefix = SolrValues.FA_PREFIX;
		String solrType = SolrValues.FA_TYPE;
		String solrDynamic = SolrFields.FA_DYNAMIC;
		String solrDynamicId = SolrFields.FA_DYNAMIC_ID;
		if (xmlType == XmlType.EAD_HG) {
			solrPrefix = SolrValues.HG_PREFIX;
			solrType = SolrValues.HG_TYPE;
			solrDynamic = SolrFields.HG_DYNAMIC;
			solrDynamicId = SolrFields.HG_DYNAMIC_ID;
		} else if (xmlType == XmlType.EAD_SG) {
			solrPrefix = SolrValues.SG_PREFIX;
			solrType = SolrValues.SG_TYPE;
			solrDynamic = SolrFields.SG_DYNAMIC;
			solrDynamicId = SolrFields.SG_DYNAMIC_ID;
		}
		SolrInputDocument doc1 = new SolrInputDocument();
		if (publishData.isArchdesc()){
			add(doc1, SolrFields.ID, solrPrefix + publishData.getId());
		}else {
			add(doc1, SolrFields.ID, SolrValues.C_LEVEL_PREFIX + publishData.getId());
		}
		if (publishData.getParentId() == null) {
			add(doc1, SolrFields.PARENT_ID, solrPrefix + ead.getId());
		} else {
			add(doc1, SolrFields.PARENT_ID, SolrValues.C_LEVEL_PREFIX + publishData.getParentId());
		}
		add(doc1, SolrFields.UNITID, unitid);
		add(doc1, SolrFields.OTHERUNITID, otherunitid);
		if (StringUtils.isNotBlank(unitid)){
			doc1.addField(SolrFields.DUPLICATE_UNITID, publishData.isDuplicateUnitid());
		}
		add(doc1, SolrFields.SCOPECONTENT, scopecontent);
		add(doc1, SolrFields.TITLE, title);
		if (publishData.isArchdesc()){
			add(doc1, SolrFields.LEVEL, LEVEL_ARCHDESC);
		}else {
			add(doc1, SolrFields.LEVEL, LEVEL_CLEVEL);
		}
		add(doc1, SolrFields.START_DATE, sdate);
		add(doc1, SolrFields.END_DATE, edate);
		add(doc1, SolrFields.ALTERDATE, alterdate);
		if (StringUtils.isBlank(alterdate)) {
			add(doc1, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
		} else {
			if (StringUtils.isBlank(sdate)) {
				add(doc1, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_OTHER_DATE);
			} else {
				add(doc1, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED);
			}
		}
		add(doc1, SolrFields.COUNTRY, archivalinstitution.getCountry().getCname().replace(" ", "_") + COLON + SolrValues.TYPE_GROUP + COLON + archivalinstitution.getCountry().getId());
		doc1.addField(SolrFields.COUNTRY_ID, archivalinstitution.getCountry().getId());
		add(doc1, SolrFields.REPOSITORY_CODE, archivalinstitution.getRepositorycode());
		add(doc1, SolrFields.LANGUAGE, language);
		add(doc1, SolrFields.LANGMATERIAL, langmaterial);
		// deprecated
		add(doc1, SolrFields.AI, archivalinstitution.getAiname() + COLON + archivalinstitution.getAiId());
		doc1.addField(SolrFields.AI_ID, archivalinstitution.getAiId());
		add(doc1, SolrFields.EADID, eadidstring);
		add(doc1, SolrFields.UNITID_OF_FOND, unitidfond);
		doc1.addField(SolrFields.DAO, dao);
		if (dao && roleDao.size() == 0) {
			doc1.addField(SolrFields.ROLEDAO, "UNSPECIFIED");
		} else if (dao) {
			doc1.addField(SolrFields.ROLEDAO, roleDao);
		}
		add(doc1, SolrFields.OTHER, otherinfo);
		doc1.addField(SolrFields.LEAF, publishData.isLeaf());

		add(doc1, SolrFields.FOND_ID, solrPrefix + ead.getId());
		add(doc1, SolrFields.TITLE_OF_FOND, fond + COLON + solrPrefix + ead.getId());
		add(doc1, SolrFields.TYPE, solrType);

		for (int i = 0; i < publishData.getUpperLevelUnittitles().size(); i++) {
			LevelInfo levelInfo = publishData.getUpperLevelUnittitles().get(i);
			String result = "";
			String id = null;
			if (i == 0) {
				result = fond + COLON + SolrValues.TYPE_GROUP + COLON + solrPrefix + ead.getId();
				id = solrPrefix + ead.getId();
			} else {
				result = levelInfo.getUnittitle() + COLON + SolrValues.TYPE_GROUP + COLON;
				result += SolrValues.C_LEVEL_PREFIX + levelInfo.getClId();
				id = SolrValues.C_LEVEL_PREFIX + levelInfo.getClId();
				result = NUMBERFORMAT.format(levelInfo.getOrderId()) + COLON + result;
			}
			add(doc1, solrDynamic + i + SolrFields.DYNAMIC_STRING_SUFFIX, result);
			add(doc1, solrDynamicId + i + SolrFields.DYNAMIC_STRING_SUFFIX, id);
		}
		if (publishData.getFullHierarchy().size() > 0) {
			Set<Map.Entry<String, Object>> entries = publishData.getFullHierarchy().entrySet();
			for (Map.Entry<String, Object> entry : entries) {
				doc1.addField(entry.getKey(), entry.getValue());
			}
		}
		doc1.addField(SolrFields.ORDER_ID, publishData.getOrderId());
		addSolrDocument(doc1);
	}



	public void commitAll(EADCounts eadCounts) throws MalformedURLException, SolrServerException, IOException {
		commitSolrDocuments();
		removeWarnings();
		ead.setTotalNumberOfDaos(eadCounts.getNumberOfDAOsBelow());
		if (eadCounts.getNumberOfDAOsBelow() == 0){
			if (ead instanceof FindingAid){
				FindingAid findingAid = (FindingAid) ead;
				if (EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana())){
					((FindingAid) ead).setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
				}
			} 
		}else {
			if (ead instanceof FindingAid){
				FindingAid findingAid = (FindingAid) ead;
				if (EuropeanaState.NO_EUROPEANA_CANDIDATE.equals(findingAid.getEuropeana())){
					((FindingAid) ead).setEuropeana(EuropeanaState.NOT_CONVERTED);
				}
			} 			
		}
		ead.setTotalNumberOfUnits(eadCounts.getNumberOfUnits());
		ead.setTotalNumberOfUnitsWithDao(eadCounts.getNumberOfUnitsWithDaosBelow());
		ContentUtils.changeSearchable(ead, true);
	}

	public void rollback() throws SolrServerException, IOException {

		String solrPrefix = SolrValues.FA_PREFIX;
		if (xmlType == XmlType.EAD_HG)
			solrPrefix = SolrValues.HG_PREFIX;
		else if (xmlType == XmlType.EAD_SG)
			solrPrefix = SolrValues.SG_PREFIX;

		rollbackSolrDocuments(SolrFields.FOND_ID + ":" + solrPrefix + ead.getId());
		Ead rollBackEad = eadDao.findById(ead.getId(), xmlType.getClazz());
		ContentUtils.changeSearchable(rollBackEad, false);
		eadDao.store(rollBackEad);
	}



	protected static Set<String> getTextsWithoutMultiplity(NodeList nodeList) {
		Set<String> results = new TreeSet<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			String text = removeUnusedCharacters(nodeList.item(i).getTextContent());
			if (StringUtils.isNotBlank(text)) {
				results.add(text.toUpperCase());
			}
		}
		return results;
	}

	@Override
	protected String getKey() {
		return this.eadidstring;
	}

	@Override
	protected AbstractSolrServerHolder getSolrServerHolder() {
		return EadSolrServerHolder.getInstance();
	}

}
