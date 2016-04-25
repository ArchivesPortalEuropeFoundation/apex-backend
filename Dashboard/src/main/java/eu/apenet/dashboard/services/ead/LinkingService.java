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
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.io.IOException;
import java.util.logging.Level;
import org.xml.sax.SAXException;

public class LinkingService {

    protected static final Logger LOGGER = Logger.getLogger(LinkingService.class);

    public static final String PREFIX_UNITID = "unitid";
    public static final String TITLE_TITLEPROPER = "titleproper";

    public static HgSgFaRelation getNewLink(Ead hgOrSg, CLevel clevel) {
        if (hgOrSg instanceof HoldingsGuide || hgOrSg instanceof SourceGuide) {
            ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
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
                Ead linkedFindingAid = linkedFindingAids.get(0);
                HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
                hgSgFaRelation.setFaId(linkedFindingAid.getId());
                hgSgFaRelation.setAiId(hgOrSg.getAiId());
                hgSgFaRelation.setHgSgClevelId(clevel.getId());
                if (hgOrSg instanceof HoldingsGuide) {
                    hgSgFaRelation.setHgId(hgOrSg.getId());
                } else if (hgOrSg instanceof SourceGuide) {
                    hgSgFaRelation.setSgId(hgOrSg.getId());
                }
                return hgSgFaRelation;
            }
        }
        return null;
    }

    public static boolean linkWithoutCommit(Ead hgOrSg, CLevel clevel) {
        HgSgFaRelation hgSgFaRelation = getNewLink(hgOrSg, clevel);
        if (hgSgFaRelation == null) {
            return false;
        } else {
            JpaUtil.getEntityManager().persist(hgSgFaRelation);
            return true;
        }

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
                    hgSgFaRelation.setHgSgClevelId(clevel.getId());
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

    public static boolean addAllFindingaidsToHgOrSg(ContentSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions();
        eadSearchOptionsNew.setContentClass(FindingAid.class);
        eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
        eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
        eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
        return addFindingaidsToHgOrSgInternal(eadSearchOptionsNew, ecId, parentCLevelId, prefixMethod, titleMethod);
    }

    public static boolean addFindingaidsToHgOrSg(ContentSearchOptions eadSearchOptions, Integer id, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setId(id);
        return addFindingaidsToHgOrSgInternal(eadSearchOptionsNew, ecId, parentCLevelId, prefixMethod, titleMethod);
    }

    public static boolean addFindingaidsToHgOrSg(ContentSearchOptions eadSearchOptions, List<Integer> ids, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setIds(ids);
        return addFindingaidsToHgOrSgInternal(eadSearchOptionsNew, ecId, parentCLevelId, prefixMethod, titleMethod);
    }

    public static boolean addFindingaidsToHgOrSg(ContentSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
        return addFindingaidsToHgOrSgInternal(new ContentSearchOptions(eadSearchOptions), ecId, parentCLevelId, prefixMethod, titleMethod);
    }

    private static boolean addFindingaidsToHgOrSgInternal(ContentSearchOptions eadSearchOptions, Long ecId, Long parentCLevelId, String prefixMethod, String titleMethod) {
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
            if (parentCLevelId != null) {
                sizeChildren = DAOFactory.instance().getCLevelDAO().countChildCLevels(parentCLevelId).intValue();
            } else {
                sizeChildren = DAOFactory.instance().getCLevelDAO().countTopCLevels(ecId).intValue();
            }
            JpaUtil.beginDatabaseTransaction();
            List<Ead> eads = DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);
            while (eads.size() > 0) {
                Ead ead = eads.get(0);
                InputStream xslIs = TransformationTool.class.getResourceAsStream("/xsl/fa2hg-extended.xsl");
                Source xsltSource = new StreamSource(xslIs);
                String filePath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath();
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
                hgSgFaRelation.setHgSgClevelId(cLevel.getId());
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

    public static List<Ead> getFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, Long ecId) {
        return getFindingaidsToLinkToHgOrSgInternal(new ContentSearchOptions(eadSearchOptions), ecId);
    }

    public static long countFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, Long ecId) {
        return countFindingaidsToLinkToHgOrSgInternal(new ContentSearchOptions(eadSearchOptions), ecId);

    }

    public static List<Ead> getFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, List<Integer> ids, Long ecId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setIds(ids);
        return getFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);
    }

    public static long countFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, List<Integer> ids, Long ecId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setIds(ids);
        return countFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);

    }

    public static List<Ead> getFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, Integer id, Long ecId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setId(id);
        return getFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);
    }

    public static long countFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, Integer id, Long ecId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setId(id);
        return countFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);

    }

    private static List<Ead> getFindingaidsToLinkToHgOrSgInternal(ContentSearchOptions eadSearchOptions, Long ecId) {
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

    private static long countFindingaidsToLinkToHgOrSgInternal(ContentSearchOptions eadSearchOptions, Long ecId) {
        EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById(ecId);
        Ead hgOrSg = eadContent.getEad();
        SecurityContext.get().checkAuthorized(hgOrSg);
        eadSearchOptions.setLinked(false);
        eadSearchOptions.setPageSize(0);
        eadSearchOptions.setLinkedId(hgOrSg.getId());
        eadSearchOptions.setLinkedWithEadClass(hgOrSg.getEadClass());
        return DAOFactory.instance().getEadDAO().countEads(eadSearchOptions);

    }

    public static List<Ead> getAllFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, Long ecId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions();
        eadSearchOptionsNew.setContentClass(FindingAid.class);
        eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
        eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
        eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
        return getFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);
    }

    public static long countAllFindingaidsToLinkToHgOrSg(ContentSearchOptions eadSearchOptions, Long ecId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions();
        eadSearchOptionsNew.setContentClass(FindingAid.class);
        eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
        eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
        eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
        return countFindingaidsToLinkToHgOrSgInternal(eadSearchOptionsNew, ecId);

    }

    public static boolean removeAllFindingaidsFromHg(ContentSearchOptions eadSearchOptions, Integer hgId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions();
        eadSearchOptionsNew.setContentClass(FindingAid.class);
        eadSearchOptionsNew.setArchivalInstitionId(eadSearchOptions.getArchivalInstitionId());
        eadSearchOptionsNew.setOrderByAscending(eadSearchOptions.isOrderByAscending());
        eadSearchOptionsNew.setOrderByField(eadSearchOptions.getOrderByField());
        return removeFindingaidsFromHgInternal(eadSearchOptionsNew, hgId);
    }

    public static boolean removeFindingaidsFromHg(ContentSearchOptions eadSearchOptions, Integer id, Integer hgId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setId(id);
        return removeFindingaidsFromHgInternal(eadSearchOptionsNew, hgId);
    }

    public static boolean removeFindingaidsFromHg(ContentSearchOptions eadSearchOptions, List<Integer> ids, Integer hgId) {
        ContentSearchOptions eadSearchOptionsNew = new ContentSearchOptions(eadSearchOptions);
        eadSearchOptionsNew.setIds(ids);
        return removeFindingaidsFromHgInternal(eadSearchOptionsNew, hgId);
    }

    public static boolean removeFindingaidsFromHg(ContentSearchOptions eadSearchOptions, Integer hgId) {
        return removeFindingaidsFromHgInternal(new ContentSearchOptions(eadSearchOptions), hgId);
    }

    private static boolean removeFindingaidsFromHgInternal(ContentSearchOptions eadSearchOptions, Integer hgId) {
        List<Ead> eads = DAOFactory.instance().getEadDAO().getEads(eadSearchOptions);
        while (eads.size() > 0) {
            InputStream fileIs = null;
            try {
                Ead ead = eads.get(0);
                Integer faId = ead.getId();
                HgSgFaRelation hgSgFaRelation = DAOFactory.instance().getHgSgFaRelationDAO().getHgSgFaRelationByHgFaCombination(hgId, faId);
                Long cid = hgSgFaRelation.getHgSgClevelId();
                CLevel clevel = DAOFactory.instance().getCLevelDAO().findById(cid);

                InputStream xslIs = TransformationTool.class.getResourceAsStream("/xsl/removefafromhg.xsl");
                Source xsltSource = new StreamSource(xslIs);
                String filePath = APEnetUtilities.getConfig().getRepoDirPath() + DAOFactory.instance().getHoldingsGuideDAO().findById(hgId).getPathApenetead();
                fileIs = FileUtils.openInputStream(new File(filePath));
                Map<String, String> params = new HashMap<String, String>();
                params.put("cident", clevel.getHrefEadid());
                StringWriter stringWriter = new StringWriter();
                TransformationTool.createTransformation(fileIs, stringWriter, xsltSource, params);
                /*
                3. load physical file of HG/SG
                4. remove respective c-level from file (should only affect leaf nodes)
                */
//            JpaUtil.beginDatabaseTransaction();
//            DAOFactory.instance().getCLevelDAO().delete(clevel);
//            DAOFactory.instance().getHgSgFaRelationDAO().delete(hgSgFaRelation);
//            JpaUtil.commitDatabaseTransaction();
                eads.remove(0);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(LinkingService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                java.util.logging.Logger.getLogger(LinkingService.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileIs.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(LinkingService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
}
