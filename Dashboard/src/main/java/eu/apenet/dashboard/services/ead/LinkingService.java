package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
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
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class LinkingService {
	protected static final Logger LOGGER = Logger.getLogger(LinkingService.class);

	public static boolean linkWithoutCommit(Ead hgOrSg, CLevel clevel) {
		if (hgOrSg instanceof HoldingsGuide || hgOrSg instanceof SourceGuide) {
			Ead linkedFindingAid = DAOFactory.instance().getEadDAO()
					.getEadByEadid(FindingAid.class, hgOrSg.getAiId(), clevel.getHrefEadid());
			if (linkedFindingAid != null) {
				HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
				hgSgFaRelation.setFaId(linkedFindingAid.getId());
				hgSgFaRelation.setAiId(hgOrSg.getAiId());
				hgSgFaRelation.setHgSgClevelId(clevel.getClId());
				if (hgOrSg instanceof HoldingsGuide) {
					hgSgFaRelation.setHgId(hgOrSg.getId());
				} else if (hgOrSg instanceof SourceGuide) {
					hgSgFaRelation.setSgId(hgOrSg.getId());
				}
				HibernateUtil.getDatabaseSession().save(hgSgFaRelation);
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

	public static boolean addFindingaidToHgOrSg(Integer id, Integer aiId, Long ecId, Long parentCLevelId) {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(id);
		return addFindingaidsToHgOrSg(ids, aiId, ecId, parentCLevelId);
	}

	public static boolean addFindingaidsToHgOrSg(List<Integer> ids, Integer aiId, Long ecId, Long parentCLevelId) {

		EadSearchOptions eadSearchOptions = new EadSearchOptions();
		eadSearchOptions.setPageSize(0);
		eadSearchOptions.setEadClazz(FindingAid.class);
		eadSearchOptions.setArchivalInstitionId(aiId);
		if (ids != null && ids.size() > 0) {
			eadSearchOptions.setIds(ids);
		}
		return addFindingaidsToHgOrSg(eadSearchOptions, ecId, parentCLevelId);
	}

	public static boolean addFindingaidsToHgOrSg(EadSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId) {

		SecurityContext.get().checkAuthorized(eadSearchOptions.getArchivalInstitionId());
		EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById(ecId);
		SecurityContext.get().checkAuthorized(eadContent.getEad());
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
			int size = 0;
			while ((size = eads.size()) > 0) {
				Ead ead = eads.get(size - 1);
				InputStream xslIs = TransformationTool.class.getResourceAsStream("/xsl/fa2hg.xsl");
				Source xsltSource = new StreamSource(xslIs);
				String filePath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();
				InputStream fileIs = FileUtils.openInputStream(new File(filePath));
				Map<String, String> params = new HashMap<String, String>();
				params.put("addXMLNS", "true");
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
				JpaUtil.getEntityManager().persist(cLevel);
				HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
				hgSgFaRelation.setFaId(ead.getId());
				hgSgFaRelation.setAiId(ead.getAiId());
				hgSgFaRelation.setHgSgClevelId(cLevel.getClId());
				hgSgFaRelation.setHgId(eadContent.getHgId());
				hgSgFaRelation.setSgId(eadContent.getSgId());
				JpaUtil.getEntityManager().persist(hgSgFaRelation);
				eads.remove(size - 1);
			}
			JpaUtil.commitDatabaseTransaction();

			return true;
		} catch (Exception e) {
			LOGGER.error("Unable to link FA to HG/SG", e);
			JpaUtil.rollbackDatabaseTransaction();
			return false;
		}
	}
}
