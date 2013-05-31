package eu.apenet.dashboard.services.ead.publish;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.solr.UpdateSolrServerHolder;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
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

public class SolrPublisher {

	private static final String WHITE_SPACE = " ";
	public static final String COLON = ":";
	private static final Logger LOG = Logger.getLogger(SolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");
	// public static final DecimalFormat SIMPLE_NUMBERFORMAT = new
	// DecimalFormat("000");
	//private String provider;
	//private Country country;
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
	private Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
	private static final int MAX_NUMBER_OF_PENDING_DOCS = 200;
	private String pathApenetead;
	private boolean isFinalPath;
	private String currentPath;
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
	private static XPathExpression cLevelAttributeExpression;
	private static XPathExpression cDidExpression;
	private static XPathExpression cUnitidExpression;
	private static XPathExpression cOtherUnitidExpression;
	private static XPathExpression cUnittitleExistExpression;
	private static XPathExpression cUnittitleExpression;
	private static XPathExpression cOtherUnittitleExpression;
	private static XPathExpression cUnitdateExpression;
	private static XPathExpression cOtherUnitdateExpression;
	private static XPathExpression cUnitdateNormalExpression;
	private static XPathExpression cLangmaterialExpression;
	private static XPathExpression cDidOtherExpression;
	private static XPathExpression cScopecontentExpression;
	private static XPathExpression cOtherExpression;
	private static XPathExpression displayIntroExpression;
	private static XPathExpression displayDidExpression;
	static {
		try {
			XPATH.setNamespaceContext(new EADNamespaceContext());
			eadidExpression = XPATH.compile("/ead:ead/ead:eadheader/ead:eadid");
			eadidIdentifierExpression = XPATH.compile("@identifier");
			languageExpression = XPATH
					.compile("/ead:ead/ead:eadheader/ead:profiledesc/ead:langusage/ead:language/@langcode");
			archdescLangmaterialExpression = XPATH
					.compile("/ead:ead/ead:archdesc/ead:did/ead:langmaterial/ead:language/@langcode");
			archdescUnitidExpression = XPATH.compile("/ead:ead/ead:archdesc/ead:did/ead:unitid/text()");
			fondTitleExpression = XPATH
					.compile("/ead:ead/ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper[1]//text()");
			textBelowExpression = XPATH.compile("./text()");
			countNumberOfDAOsExpression = XPATH.compile("count(//ead:dao[not(@xlink:title='thumbnail')])");
			daoRoleAttributeExpression = XPATH.compile("./ead:dao/@xlink:role");
			// daoRoleAttributeExpression =
			// XPATH.compile("./ead:dao[not(@xlink:title='thumbnail')]/@xlink:role");
			cLevelAttributeExpression = XPATH.compile("/ead:c/@level");
			cDidExpression = XPATH.compile("/ead:c/ead:did");
			cUnitidExpression = XPATH.compile("./ead:unitid[@type='call number']/text()");
			cOtherUnitidExpression = XPATH
					.compile("./ead:unitid[@type='former call number' or @type='file reference']/text()");
			cUnittitleExistExpression = XPATH.compile("./ead:unittitle");
			cUnittitleExpression = XPATH.compile("./ead:unittitle[1]//text()");
			cOtherUnittitleExpression = XPATH.compile("./ead:unittitle[position() > 1]//text()");
			cUnitdateExpression = XPATH.compile("./ead:unitdate/text()");
			cOtherUnitdateExpression = XPATH.compile("./ead:unitdate[position() > 1]//text()");
			cUnitdateNormalExpression = XPATH.compile("./ead:unitdate/@normal");
			cLangmaterialExpression = XPATH.compile("./ead:langmaterial/ead:language/@langcode");
			cDidOtherExpression = XPATH
					.compile("/ead:c/ead:did/node()[not(name()='unittitle' or name()='unitid' or name()='unitdate' or name()='dao')]//text()");
			cScopecontentExpression = XPATH.compile("/ead:c/ead:scopecontent//text()");
			cOtherExpression = XPATH.compile("/ead:c/node()[not(name()='scopecontent' or name()='did')]//text()");
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
		} catch (XPathExpressionException e) {
			LOG.info(e.getMessage(), e);
		}
	}

	public SolrPublisher(Ead ead) {
		this.ead = ead;
		xmlType = XmlType.getEadType(ead);
		eadDao = DAOFactory.instance().getEadDAO();
	}

	public long parseHeader(EadContent eadContent) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		long numberOfDaos = 0l;
		pathApenetead = ead.getPathApenetead();
		archivalinstitution = ead.getArchivalInstitution();
		isFinalPath = true;
		currentPath = APEnetUtilities.getConfig().getRepoDirPath() + pathApenetead;
		/**
		 * Check the time and check if the backup file exists. If this file
		 * exists, then it cannot index, for a period.
		 **/
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(eadContent.getXml())));
		doc.getDocumentElement().normalize();
		// Obtain the archival institution.

		numberOfDaos = extractGeneralData(doc, eadContent);

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
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(indexData.getXml())));
		doc.getDocumentElement().normalize();
		return parseCLevel(doc, indexData);
	}

	private long parseCLevel(Document document, PublishData indexData) throws Exception {
		String startdate = null;
		String enddate = null;
		String alterdate = "";
		String attributeLevel = (String) cLevelAttributeExpression.evaluate(document, XPathConstants.STRING);
		// did parsing
		Node didNode = (Node) cDidExpression.evaluate(document, XPathConstants.NODE);
		String unitid = removeUnusedCharacters((String) cUnitidExpression.evaluate(didNode, XPathConstants.STRING));
		String otherunitid = getText((NodeList) cOtherUnitidExpression.evaluate(didNode, XPathConstants.NODESET));

		String unitdatenormal = (String) cUnitdateNormalExpression.evaluate(didNode, XPathConstants.STRING);
		Double numberOfDaos = (Double) countNumberOfDAOsExpression.evaluate(didNode, XPathConstants.NUMBER);
		boolean hasDao = numberOfDaos != null && numberOfDaos > 0;
		// String roleDao = (String)
		// daoRoleAttributeExpression.evaluate(didNode, XPathConstants.STRING);
		Set<String> roleDao = getTextsWithoutMultiplity((NodeList) daoRoleAttributeExpression.evaluate(didNode,
				XPathConstants.NODESET));
		String clevelLangmaterial = getText((NodeList) cLangmaterialExpression
				.evaluate(didNode, XPathConstants.NODESET));
		String otherinfo = "";
		String unittitle = "";
		boolean hasUnittitle = (Boolean) cUnittitleExistExpression.evaluate(didNode, XPathConstants.BOOLEAN);
		if (hasUnittitle) {
			unittitle = getText((NodeList) cUnittitleExpression.evaluate(didNode, XPathConstants.NODESET));
			otherinfo += WHITE_SPACE
					+ getText((NodeList) cOtherUnittitleExpression.evaluate(didNode, XPathConstants.NODESET));
		}
		String unitdate = removeUnusedCharacters((String) cUnitdateExpression.evaluate(didNode, XPathConstants.STRING));
		if (StringUtils.isNotBlank(unitdate)) {
			otherinfo += WHITE_SPACE
					+ getText((NodeList) cOtherUnitdateExpression.evaluate(didNode, XPathConstants.NODESET));
		}
		otherinfo += WHITE_SPACE + getText((NodeList) cDidOtherExpression.evaluate(document, XPathConstants.NODESET));
		// other parsing
		String scopecontent = getText((NodeList) cScopecontentExpression.evaluate(document, XPathConstants.NODESET));
		otherinfo += WHITE_SPACE + getText((NodeList) cOtherExpression.evaluate(document, XPathConstants.NODESET));

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

		indexNode(indexData, unitid, otherunitid, scopecontent, unittitle, attributeLevel, startdate, enddate,
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

	private static String obtainDate(String onedate, boolean isStartDate) {
		String year = null;
		String month = null;
		String day = null;
		try {
			if (onedate.contains("-")) {
				String[] list = onedate.split("-");
				if (list.length >= 1) {
					year = list[0];
				}
				if (list.length >= 2) {
					month = list[1];
				}
				if (list.length >= 3) {
					day = list[2];
				}
			} else {
				if (onedate.length() >= 4) {
					year = onedate.substring(0, 4);
				}
				if (onedate.length() >= 6) {
					month = onedate.substring(4, 6);
				}
				if (onedate.length() >= 8) {
					day = onedate.substring(6, 8);
				}
			}
			return obtainDate(year, month, day, isStartDate);
		} catch (Exception ex) {
			LOG.error("Error trying to obtain Date in Indexer: " + ex.getMessage());
		}
		return null;
	}

	private static String obtainDate(String yearString, String monthString, String dayString, boolean isStartDate) {
		if (yearString != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Integer year = new Integer(yearString);
			Integer month = null;
			Integer day = null;
			if (monthString != null) {
				month = new Integer(monthString);
			}
			if (dayString != null) {
				day = new Integer(dayString);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			calendar.setTimeInMillis(0);
			calendar.set(Calendar.YEAR, year);
			if (isStartDate) {
				if (month == null) {
					calendar.set(Calendar.MONTH, 0);
				} else {
					calendar.set(Calendar.MONTH, month - 1);
				}
				if (day == null) {
					calendar.set(Calendar.DAY_OF_MONTH, 1);
				} else {
					calendar.set(Calendar.DAY_OF_MONTH, day);
				}
				return dateFormat.format(calendar.getTime()) + "T00:00:01Z";
			} else {
				if (month == null) {
					calendar.set(Calendar.MONTH, 11);
				} else {
					calendar.set(Calendar.MONTH, month - 1);
				}
				if (day == null) {
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				} else {
					calendar.set(Calendar.DAY_OF_MONTH, day);
				}
				return dateFormat.format(calendar.getTime()) + "T23:59:59Z";
			}
		}
		return null;
	}

	private void indexNode(PublishData indexData, String unitid, String otherunitid, String scopecontent, String title,
			String level, String sdate, String edate, String alterdate, boolean dao, Set<String> roleDao,
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
		add(doc1, SolrFields.ID, SolrValues.C_LEVEL_PREFIX + indexData.getClId());
		if (indexData.getParentId() == null) {
			add(doc1, SolrFields.PARENT_ID, solrPrefix + ead.getId());
		} else {
			add(doc1, SolrFields.PARENT_ID, SolrValues.C_LEVEL_PREFIX + indexData.getParentId());
		}
		add(doc1, SolrFields.UNITID, unitid);
		add(doc1, SolrFields.OTHERUNITID, otherunitid);
		add(doc1, SolrFields.SCOPECONTENT, scopecontent);
		add(doc1, SolrFields.TITLE, title);
		add(doc1, SolrFields.LEVEL, level);
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
		add(doc1, SolrFields.COUNTRY, archivalinstitution.getCountry().getCname() + COLON + SolrValues.TYPE_GROUP + COLON + archivalinstitution.getCountry().getId());
		doc1.addField(SolrFields.COUNTRY_ID, archivalinstitution.getCountry().getId());
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
		doc1.addField(SolrFields.LEAF, indexData.isLeaf());

		add(doc1, SolrFields.FOND_ID, solrPrefix + ead.getId());
		add(doc1, SolrFields.TITLE_OF_FOND, fond + COLON + solrPrefix + ead.getId());
		add(doc1, SolrFields.TYPE, solrType);

		for (int i = 0; i < indexData.getUpperLevelUnittitles().size(); i++) {
			LevelInfo levelInfo = indexData.getUpperLevelUnittitles().get(i);
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
		if (indexData.getFullHierarchy().size() > 0) {
			Set<Map.Entry<String, Object>> entries = indexData.getFullHierarchy().entrySet();
			for (Map.Entry<String, Object> entry : entries) {
				doc1.addField(entry.getKey(), entry.getValue());
			}
		}
		doc1.addField(SolrFields.ORDER_ID, indexData.getOrderId());
		docs.add(doc1);
		if (docs.size() == MAX_NUMBER_OF_PENDING_DOCS) {
			UpdateSolrServerHolder.getInstance().add(docs);
			docs = new ArrayList<SolrInputDocument>();
		}
	}

	private static void add(SolrInputDocument doc, String name, String value) {
		if (StringUtils.isNotBlank(value)) {
			doc.addField(name, value);
		}
	}

	public void commitAll(EADCounts eadCounts) throws MalformedURLException, SolrServerException, IOException {
		if (docs.size() > 0) {
			UpdateSolrServerHolder.getInstance().add(docs);
			docs = new ArrayList<SolrInputDocument>();
		}
		UpdateSolrServerHolder.getInstance().commit();
		removeWarnings();
		if (!isFinalPath) {
			String[] otherlist = null;
			if (currentPath.contains(APEnetUtilities.FILESEPARATOR)) {
				otherlist = pathApenetead.split(APEnetUtilities.FILESEPARATOR);
			}
			String foldername = "";
			if (otherlist[3] != null) {
				foldername = otherlist[3].trim();
			}
			foldername = foldername.replace(".xml", "");
			ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
			String theCountry = archivalInstitution.getCountry().getIsoname().trim();
			Integer institutionId = archivalInstitution.getAiId();
			String institutionStr = institutionId.toString().trim();
			String finalpath2 = APEnetUtilities.FILESEPARATOR + theCountry + APEnetUtilities.FILESEPARATOR
					+ institutionStr + APEnetUtilities.FILESEPARATOR + "FA" + APEnetUtilities.FILESEPARATOR
					+ foldername + APEnetUtilities.FILESEPARATOR + foldername + ".xml";
			if (xmlType == XmlType.EAD_HG || xmlType == XmlType.EAD_SG) {
				finalpath2 = APEnetUtilities.FILESEPARATOR + theCountry + APEnetUtilities.FILESEPARATOR
						+ institutionStr + APEnetUtilities.FILESEPARATOR + "HG" + APEnetUtilities.FILESEPARATOR
						+ foldername + ".xml";
			}
			insertFileToRepository(currentPath, APEnetUtilities.getConfig().getRepoDirPath() + finalpath2);
			ead.setPathApenetead(finalpath2);
		}
		// Indexed_Not converted to ESE/EDM
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
		UpdateSolrServerHolder.getInstance().commit();

		String solrPrefix = SolrValues.FA_PREFIX;
		if (xmlType == XmlType.EAD_HG)
			solrPrefix = SolrValues.HG_PREFIX;
		else if (xmlType == XmlType.EAD_SG)
			solrPrefix = SolrValues.SG_PREFIX;

		UpdateSolrServerHolder.getInstance().deleteByQuery(SolrFields.FOND_ID + ":" + solrPrefix + ead.getId());
		Ead rollBackEad = eadDao.findById(ead.getId(), xmlType.getClazz());
		ContentUtils.changeSearchable(rollBackEad, false);
		eadDao.store(rollBackEad);

		UpdateSolrServerHolder.getInstance().commit();
	}

	/**
	 * This method deletes the destination file, copies the source file to
	 * temporal directory and finally deletes the source file if everything is
	 * ok If the source folder is empty, then the folder will be removed
	 */
	private void insertFileToRepository(String srcFilePath, String destFilePath) throws IOException {
		LOG.info("Since we changed the filePath to be always in REPO, this function should be useless, we return it here if src and dest filePath are equals");
		if (srcFilePath.equals(destFilePath)) {
			LOG.info("They are equal");
			return;
		}

		Boolean error = false;
		File srcFile = new File(srcFilePath);
		File destFile = new File(destFilePath);

		String trash = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR
				+ "Trash" + APEnetUtilities.FILESEPARATOR;
		File folderTrash = new File(trash);
		if (!folderTrash.exists())
			folderTrash.mkdir();

		try {
			if ((destFile.exists()) && (srcFile).exists()) {

				String[] otherlist = null;
				if (destFilePath.contains(APEnetUtilities.FILESEPARATOR)) {
					otherlist = destFilePath.split(APEnetUtilities.FILESEPARATOR);
				}
				String fileName = otherlist[otherlist.length - 1];
				File file = new File(trash + fileName);
				if (!file.exists()) {
					FileUtils.moveFile(destFile, file);
					LOG.info(destFilePath + " moved to " + trash + fileName);
				}
				// FileUtils.forceDelete(destFile);
			}
			FileUtils.copyFile(srcFile, destFile);
			LOG.info("File: " + srcFilePath + " copied to " + destFilePath);
			// FileUtils.forceDelete(srcFile);
			String[] otherlist = null;
			if (srcFilePath.contains(APEnetUtilities.FILESEPARATOR)) {
				otherlist = srcFilePath.split(APEnetUtilities.FILESEPARATOR);
			}

			String fileName = otherlist[otherlist.length - 1];
			File file = new File(trash + fileName);
			if (!file.exists()) {
				FileUtils.moveFile(srcFile, file);
				LOG.info(srcFilePath + " moved to " + trash + fileName);
			} else
				FileUtils.forceDelete(srcFile);

			srcFile = null;
			destFile = null;
		} catch (IOException ex) {
			error = true;
			LOG.error("Insert File to Repository. Error: " + ex.getMessage(), ex);
			throw ex;
		} finally {
			if (!error) {
				String[] otherlist = null;
				if (srcFilePath.contains(APEnetUtilities.FILESEPARATOR)) {
					otherlist = srcFilePath.split(APEnetUtilities.FILESEPARATOR);
				}
				String fileName = otherlist[otherlist.length - 1];
				File file = new File(trash + fileName);
				if (file.exists())
					FileUtils.forceDelete(file);
			}
		}
	}

	private static String removeUnusedCharacters(String input) {
		if (input != null) {
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", "");
			return result.trim();
		} else {
			return null;
		}

	}

	private static String getText(NodeList nodeList) {
		String result = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			result += WHITE_SPACE + nodeList.item(i).getTextContent();
		}
		return removeUnusedCharacters(result);
	}

	private static Set<String> getTextsWithoutMultiplity(NodeList nodeList) {
		Set<String> results = new TreeSet<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			String text = removeUnusedCharacters(nodeList.item(i).getTextContent());
			if (StringUtils.isNotBlank(text)) {
				results.add(text);
			}
		}
		return results;
	}

}
