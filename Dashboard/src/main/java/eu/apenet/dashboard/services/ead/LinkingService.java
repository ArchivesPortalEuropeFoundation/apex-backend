package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.ead.xml.EADNamespaceContext;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class LinkingService {
	protected static final Logger LOGGER = Logger.getLogger(LinkingService.class);

    public static final String PREFIX_UNITID = "unitid";
    public static final String TITLE_TITLEPROPER = "titleproper";
	public static boolean linkWithoutCommit(Ead hgOrSg, CLevel clevel) {
		if (hgOrSg instanceof HoldingsGuide || hgOrSg instanceof SourceGuide) {
			EadSearchOptions eadSearchOptions = new EadSearchOptions();
			eadSearchOptions.setArchivalInstitionId(hgOrSg.getAiId());
			eadSearchOptions.setContentClass(FindingAid.class);
			eadSearchOptions.setEadid(clevel.getHrefEadid());
			eadSearchOptions.setLinkedWithEadClass(hgOrSg.getEadClass());
			eadSearchOptions.setLinkedId(hgOrSg.getId());
			eadSearchOptions.setLinked(false);
			eadSearchOptions.setPageSize(1);
			eadSearchOptions.setPageNumber(1);
			List<Ead> linkedFindingAids = DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);
			if (linkedFindingAids.size() > 0) {
				Ead linkedFindingAid  = linkedFindingAids.get(0);
				HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
				hgSgFaRelation.setFaId(linkedFindingAid.getId());
				hgSgFaRelation.setAiId(hgOrSg.getAiId());
				hgSgFaRelation.setHgSgClevelId(clevel.getClId());
				if (hgOrSg instanceof HoldingsGuide) {
					hgSgFaRelation.setHgId(hgOrSg.getId());
				} else if (hgOrSg instanceof SourceGuide) {
					hgSgFaRelation.setSgId(hgOrSg.getId());
				}
				JpaUtil.getEntityManager().persist(hgSgFaRelation);
				return true;
			}
		}
		return false;
	}

	public static boolean linkWithHgOrSg(Ead ead) {
		if (ead instanceof FindingAid) {
			try {
				FindingAid findingAid = (FindingAid) ead;
				CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
				List<CLevel> clevels = clevelDAO.getClevelsFromSgOrHg(findingAid.getAiId(), findingAid.getEadid());
				JpaUtil.beginDatabaseTransaction();
				for (CLevel clevel : clevels) {
					HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
					hgSgFaRelation.setFaId(findingAid.getId());
					hgSgFaRelation.setAiId(findingAid.getAiId());
					hgSgFaRelation.setHgSgClevelId(clevel.getClId());
					EadContent eadContent = clevel.getEadContent();
					hgSgFaRelation.setHgId(eadContent.getHgId());
					hgSgFaRelation.setSgId(eadContent.getSgId());
					JpaUtil.getEntityManager().persist(hgSgFaRelation);
				}
				JpaUtil.commitDatabaseTransaction();
			} catch (Exception e) {
				LOGGER.error("Could not link " + ead.getEadid() + " to HG or SG");
				JpaUtil.rollbackDatabaseTransaction();
			}
			return true;
		}
		return false;
	}


	public static boolean addAllFindingaidsToHgOrSg(EadSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions();
		eadSearchOptionsNew.setContentClass(FindingAid.class);		
		eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
		eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
		eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
		return addFindingaidsToHgOrSgInternal(eadSearchOptionsNew,ecId, parentCLevelId, prefixMethod, titleMethod);
	}
	public static boolean addFindingaidsToHgOrSg(EadSearchOptions eadSearchOptions, Integer id,  Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions(eadSearchOptions);
		eadSearchOptionsNew.setId(id);
		return addFindingaidsToHgOrSgInternal(eadSearchOptionsNew, ecId, parentCLevelId, prefixMethod, titleMethod);
	}
	public static boolean addFindingaidsToHgOrSg(EadSearchOptions eadSearchOptions, List<Integer> ids,  Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions(eadSearchOptions);
		eadSearchOptionsNew.setIds(ids);
		return addFindingaidsToHgOrSgInternal(eadSearchOptionsNew, ecId, parentCLevelId, prefixMethod, titleMethod);
	}
	public static boolean addFindingaidsToHgOrSg(EadSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
		return addFindingaidsToHgOrSgInternal(new EadSearchOptions(eadSearchOptions), ecId, parentCLevelId, prefixMethod, titleMethod);
	}

	private static boolean addFindingaidsToHgOrSgInternal(EadSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
		EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById(ecId);
		Ead hgOrSg = eadContent.getEad();
		SecurityContext.get().checkAuthorized(hgOrSg);
		eadSearchOptions.setArchivalInstitionId(hgOrSg.getAiId());
		eadSearchOptions.setLinked(false);
		eadSearchOptions.setLinkedId(hgOrSg.getId());
		eadSearchOptions.setLinkedWithEadClass(hgOrSg.getEadClass());
		eadSearchOptions.setPageSize(0);
		try {
			XPath xPath = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
			xPath.setNamespaceContext(new EADNamespaceContext());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc;
			XPathExpression unittitleExpr = xPath.compile("/ead:c/ead:did/ead:unittitle/text()");
			XPathExpression unitidExpr = xPath.compile("/ead:c/ead:did/ead:unitid/text()");

			int sizeChildren = 0;
			if (parentCLevelId != null)
				sizeChildren = DAOFactory.instance().getCLevelDAO().countChildCLevels(parentCLevelId).intValue();
			else
				sizeChildren = DAOFactory.instance().getCLevelDAO().countTopCLevels(ecId).intValue();
			JpaUtil.beginDatabaseTransaction();
			List<Ead> eads = DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);
			while (eads.size() > 0) {
				Ead ead = eads.get(0);
				InputStream xslIs = TransformationTool.class.getResourceAsStream("/xsl/fa2hg-extended.xsl");
				Source xsltSource = new StreamSource(xslIs);
				String filePath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();
				InputStream fileIs = FileUtils.openInputStream(new File(filePath));
				Map<String, String> params = new HashMap<String, String>();
				params.put("title", titleMethod);
				params.put("prefix", prefixMethod);
				StringWriter stringWriter = new StringWriter();

				TransformationTool.createTransformation(fileIs, stringWriter, xsltSource, params);

				String cLevelXml = stringWriter.toString();

				doc = builder.parse(new InputSource(new StringReader(cLevelXml)));
				doc.getDocumentElement().normalize();

				String unittitleString = (String) unittitleExpr.evaluate(doc, XPathConstants.STRING);
				String unitidString = (String) unitidExpr.evaluate(doc, XPathConstants.STRING);

				CLevel cLevel = new CLevel();
				cLevel.setLeaf(true);
				cLevel.setLevel("item");
				cLevel.setXml(cLevelXml);
				cLevel.setUnitid(unitidString);
				cLevel.setUnittitle(unittitleString);
				cLevel.setOrderId(sizeChildren++);
				cLevel.setParentClId(parentCLevelId);
				cLevel.setEcId(ecId);
				cLevel.setHrefEadid(ead.getEadid());
				JpaUtil.getEntityManager().persist(cLevel);
				HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
				hgSgFaRelation.setFaId(ead.getId());
				hgSgFaRelation.setAiId(ead.getAiId());
				hgSgFaRelation.setHgSgClevelId(cLevel.getClId());
				hgSgFaRelation.setHgId(eadContent.getHgId());
				hgSgFaRelation.setSgId(eadContent.getSgId());
				JpaUtil.getEntityManager().persist(hgSgFaRelation);
				eads.remove(0);
			}
			JpaUtil.commitDatabaseTransaction();

			return true;
		} catch (Exception e) {
			LOGGER.error("Unable to link FA to HG/SG", e);
			JpaUtil.rollbackDatabaseTransaction();
			return false;
		}
	}
	public static List<Ead> getFindingaidsToLinkToHgOrSg(EadSearchOptions eadSearchOptions, Long ecId) {
		return getFindingaidsToLinkToHgOrSgInternal(new EadSearchOptions(eadSearchOptions), ecId);
	}
	public static long countFindingaidsToLinkToHgOrSg(EadSearchOptions eadSearchOptions, Long ecId) {
		return countFindingaidsToLinkToHgOrSgInternal(new EadSearchOptions(eadSearchOptions), ecId);

	}
	
	public static List<Ead> getFindingaidsToLinkToHgOrSg(EadSearchOptions eadSearchOptions, List<Integer> ids, Long ecId) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions(eadSearchOptions);
		eadSearchOptionsNew.setIds(ids);
		return getFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);
	}
	public static long countFindingaidsToLinkToHgOrSg(EadSearchOptions eadSearchOptions, List<Integer> ids, Long ecId) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions(eadSearchOptions);
		eadSearchOptionsNew.setIds(ids);
		return countFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);

	}
	public static List<Ead> getFindingaidsToLinkToHgOrSg(EadSearchOptions eadSearchOptions, Integer id, Long ecId) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions(eadSearchOptions);
		eadSearchOptionsNew.setId(id);
		return getFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);
	}
	public static long countFindingaidsToLinkToHgOrSg(EadSearchOptions eadSearchOptions, Integer id, Long ecId) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions(eadSearchOptions);
		eadSearchOptionsNew.setId(id);
		return countFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);

	}
	private static List<Ead> getFindingaidsToLinkToHgOrSgInternal(EadSearchOptions eadSearchOptions, Long ecId) {
		EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById(ecId);
		Ead hgOrSg = eadContent.getEad();
		SecurityContext.get().checkAuthorized(hgOrSg);
		eadSearchOptions.setLinked(false);
		eadSearchOptions.setLinkedId(hgOrSg.getId());
		eadSearchOptions.setLinkedWithEadClass(hgOrSg.getEadClass());
		eadSearchOptions.setPageSize(100);
		eadSearchOptions.setPageNumber(1);
		return DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);
	}
	private static long countFindingaidsToLinkToHgOrSgInternal(EadSearchOptions eadSearchOptions, Long ecId) {
		EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById(ecId);
		Ead hgOrSg = eadContent.getEad();
		SecurityContext.get().checkAuthorized(hgOrSg);
		eadSearchOptions.setLinked(false);
		eadSearchOptions.setPageSize(0);
		eadSearchOptions.setLinkedId(hgOrSg.getId());
		eadSearchOptions.setLinkedWithEadClass(hgOrSg.getEadClass());
		return DAOFactory.instance().getEadDAO().countEads(eadSearchOptions);

	}
	public static List<Ead> getAllFindingaidsToLinkToHgOrSg( EadSearchOptions eadSearchOptions, Long ecId) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions();
		eadSearchOptionsNew.setContentClass(FindingAid.class);		
		eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
		eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
		eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
		return getFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);
	}
	public static long countAllFindingaidsToLinkToHgOrSg( EadSearchOptions eadSearchOptions, Long ecId) {
		EadSearchOptions eadSearchOptionsNew = new EadSearchOptions();
		eadSearchOptionsNew.setContentClass(FindingAid.class);		
		eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
		eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
		eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
		return countFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);

	}

}
