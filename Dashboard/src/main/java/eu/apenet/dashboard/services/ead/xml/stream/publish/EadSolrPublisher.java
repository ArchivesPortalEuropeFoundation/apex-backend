package eu.apenet.dashboard.services.ead.xml.stream.publish;

import eu.apenet.commons.solr.Ead3SolrFields;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import eu.apenet.commons.solr.EadSolrServerHolder;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.XMLUtils;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.dao.TopicMappingDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.TopicMapping;
import eu.apenet.persistence.vo.Warnings;

public class EadSolrPublisher {

    private static final String WHITE_SPACE = " ";
    public static final String COLON = ":";
    private static final Logger LOG = Logger.getLogger(EadSolrPublisher.class);
    public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");
    private int numberOfPublishedItems = 0;
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
    private long solrTime = 0l;
    private Set<String> topicsBySourceGuides = new HashSet<String>();
    private Set<String> topicsByArchdescControlAccess = new HashSet<String>();
    private Map<String, String[]> topicMappings = new HashMap<String, String[]>();
    //private List<TopicMapping> mappings;

    public EadSolrPublisher(Ead ead) {
        this.ead = ead;
        eadidstring = XMLUtils.removeUnusedCharacters(ead.getEadid());
        fond = XMLUtils.removeUnusedCharacters(ead.getTitle());
        xmlType = XmlType.getContentType(ead);
        eadDao = DAOFactory.instance().getEadDAO();
        TopicMappingDAO topicMappingDAO = DAOFactory.instance().getTopicMappingDAO();
        HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
        List<TopicMapping> mappings = topicMappingDAO.getTopicMappingsByAiId(ead.getAiId());
        for (TopicMapping mapping : mappings) {
            if (mapping.getSgId() != null) {
                if (ead instanceof FindingAid) {
                    boolean exist = hgSgFaRelationDAO.existSgFaRelations(mapping.getSgId(), ead.getId());
                    if (exist) {
                        topicsBySourceGuides.add(mapping.getTopic().getPropertyKey());
                    }
                } else if (ead instanceof SourceGuide) {
                    if (ead.getId().equals(mapping.getSgId())) {
                        topicsBySourceGuides.add(mapping.getTopic().getPropertyKey());
                    }
                }

            }
            // fill mappings
            if (StringUtils.isNotBlank(mapping.getControlaccessKeyword())) {
                String input = mapping.getControlaccessKeyword().trim().toLowerCase();
                input = input.replaceAll("\\s*\\|\\s*", "|");
                String[] keywords = input.split("\\|");
                if (keywords.length > 0) {
                    topicMappings.put(mapping.getTopic().getPropertyKey(), keywords);
                }
            }

        }
    }

    private void addTopics(Set<String> topics, Set<String> controlAccessSubjects) {
        if (controlAccessSubjects.size() > 0) {
            for (Entry<String, String[]> entry : topicMappings.entrySet()) {
                boolean found = false;
                String[] keywords = entry.getValue();
                for (int i = 0; !found && i < keywords.length; i++) {
                    if (controlAccessSubjects.contains(keywords[i])) {
                        found = true;
                        topics.add(entry.getKey());
                    }
                }
            }
        }
    }

    public long publishArchdesc(EadPublishData publishData) throws Exception {
        archivalinstitution = ead.getArchivalInstitution();
        addTopics(topicsByArchdescControlAccess, publishData.getControlAccessSubjectsOccupationsGenreforms());
        parseCLevelOrArchdesc(false, publishData);
        language = publishData.getGlobalLanguage();
        return 0;
    }

    public long publishCLevel(EadPublishData indexData) throws Exception {
        return parseCLevelOrArchdesc(true, indexData);
    }

    private long parseCLevelOrArchdesc(boolean clevel, EadPublishData publishData) throws Exception {
        String startdate = null;
        String enddate = null;
        String alterdate = "";
        String attributeLevel = "fonds";
        if (clevel) {
            attributeLevel = null;
        } else {
            if (StringUtils.isBlank(publishData.getUnitid())) {
                unitidfond = publishData.getFirstOtherUnitid();
            } else {
                unitidfond = publishData.getUnitid();
            }
            if (StringUtils.isNotBlank(unitidfond)) {
                existunitid_archdesc = true;
            }
            archdesc_langmaterial = publishData.getLangmaterial();
        }
        // did parsing
        // String roleDao = (String)
        // daoRoleAttributeExpression.evaluate(didNode, XPathConstants.STRING);
        String unitdate = publishData.getUnitdate();

        if (StringUtils.isNotBlank(publishData.getUnitid()) && "fonds".equals(attributeLevel) && !existunitid_archdesc) {
            unitidfond = publishData.getUnitid();
        }
        if (StringUtils.isNotBlank(unitdate)) {
            if (StringUtils.isNotBlank(publishData.getUnitdateNormal())) {
                try {
                    if (publishData.getUnitdateNormal().contains("/")) {
                        String[] list = publishData.getUnitdateNormal().split("/");
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
                        startdate = obtainDate(publishData.getUnitdateNormal(), true);
                        enddate = obtainDate(publishData.getUnitdateNormal(), false);
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
        if (StringUtils.isNotBlank(publishData.getLangmaterial())) {
            langmaterial = publishData.getLangmaterial();
        } else {
            langmaterial = archdesc_langmaterial;
        }

        publishNode(clevel, publishData, startdate, enddate, alterdate, langmaterial);
        return publishData.getNumberOfDaos();
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

    private void publishNode(boolean clevel, EadPublishData publishData, String sdate, String edate, String alterdate,
            String langmaterial) throws MalformedURLException, SolrServerException, IOException {
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
        if (publishData.isArchdesc()) {
            add(doc1, SolrFields.ID, solrPrefix + publishData.getId());
        } else {
            add(doc1, SolrFields.ID, SolrValues.C_LEVEL_PREFIX + publishData.getId());
        }
        if (publishData.getParentId() == null) {
            add(doc1, SolrFields.PARENT_ID, solrPrefix + ead.getId());
        } else {
            add(doc1, SolrFields.PARENT_ID, SolrValues.C_LEVEL_PREFIX + publishData.getParentId());
        }
        add(doc1, SolrFields.UNITID, publishData.getUnitid());

        doc1.setField(SolrFields.OTHERUNITID, publishData.getOtherUnitid());
        if (StringUtils.isNotBlank(publishData.getUnitid())) {
            doc1.addField(SolrFields.DUPLICATE_UNITID, publishData.isDuplicateUnitid());
        }
        add(doc1, SolrFields.SCOPECONTENT, publishData.getScopecontent());
        add(doc1, SolrFields.TITLE, publishData.getFirstUnittitle());
        if (publishData.isArchdesc()) {
            add(doc1, SolrFields.LEVEL, SolrValues.LEVEL_ARCHDESC);
        } else {
            add(doc1, SolrFields.LEVEL, SolrValues.LEVEL_CLEVEL);
        }
        add(doc1, SolrFields.START_DATE, sdate);
        add(doc1, SolrFields.END_DATE, edate);
        add(doc1, SolrFields.ALTERDATE, alterdate);
        if (StringUtils.isBlank(alterdate)) {
            add(doc1, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
        } else if (StringUtils.isBlank(sdate)) {
            add(doc1, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_OTHER_DATE);
        } else {
            add(doc1, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED);
        }
        add(doc1, SolrFields.COUNTRY, archivalinstitution.getCountry().getCname().replace(" ", "_") + COLON
                + SolrValues.TYPE_GROUP + COLON + archivalinstitution.getCountry().getId());
        doc1.addField(SolrFields.COUNTRY_ID, archivalinstitution.getCountry().getId());
        add(doc1, SolrFields.REPOSITORY_CODE, archivalinstitution.getRepositorycode());
        add(doc1, SolrFields.LANGUAGE, language);
        add(doc1, SolrFields.LANGMATERIAL, langmaterial);
        // deprecated
        add(doc1, SolrFields.AI, archivalinstitution.getAiname() + COLON + archivalinstitution.getAiId());
        doc1.addField(SolrFields.AI_ID, archivalinstitution.getAiId());
        add(doc1, SolrFields.EADID, ead.getEadid());
        add(doc1, SolrFields.UNITID_OF_FOND, unitidfond);
        boolean dao = publishData.getNumberOfDaos() > 0;
        doc1.addField(SolrFields.DAO, dao);
        LOG.debug(this.eadidstring + Ead3SolrFields.NUMBER_OF_DAO + ": " + publishData.getNumberOfDaos());
        doc1.addField(Ead3SolrFields.NUMBER_OF_DAO, publishData.getNumberOfDaosBelow());
        doc1.addField(Ead3SolrFields.NUMBER_OF_DESCENDENTS, publishData.getNumberOfDescendents());
        int numberOfAncestors = publishData.getUpperLevelUnittitles().size();
        if (publishData.isArchdesc()) {
            numberOfAncestors--;
        }
        doc1.addField(Ead3SolrFields.NUMBER_OF_ANCESTORS, numberOfAncestors);
        if (dao && publishData.getRoledao().size() == 0) {
            doc1.addField(SolrFields.ROLEDAO, SolrValues.ROLE_DAO_UNSPECIFIED);
        } else if (dao) {
            doc1.addField(SolrFields.ROLEDAO, publishData.getRoledao());
        }
        add(doc1, SolrFields.OTHER, publishData.getOtherinfo());
        doc1.addField(SolrFields.LEAF, publishData.isLeaf());

        add(doc1, SolrFields.FOND_ID, solrPrefix + ead.getId());
        add(doc1, SolrFields.TITLE_OF_FOND, fond + COLON + solrPrefix + ead.getId());
        add(doc1, SolrFields.TYPE, solrType);
        Set<String> topics = new HashSet<String>();
        topics.addAll(topicsBySourceGuides);
        topics.addAll(topicsByArchdescControlAccess);
        if (clevel) {
            addTopics(topics, publishData.getControlAccessSubjectsOccupationsGenreforms());
        }
        if (topics.size() > 0) {
            doc1.addField(SolrFields.TOPIC_FACET, topics);
        }
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

        ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
        ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(archivalinstitution.getAiId());

        doc1.addField(SolrFields.OPEN_DATA, archivalInstitution.isOpenDataEnabled());
        docs.add(doc1);
        if (docs.size() == MAX_NUMBER_OF_PENDING_DOCS) {
            solrTime += EadSolrServerHolder.getInstance().add(docs);
            docs = new ArrayList<SolrInputDocument>();
            numberOfPublishedItems += MAX_NUMBER_OF_PENDING_DOCS;
            LOG.debug(this.eadidstring + " #published: " + numberOfPublishedItems);
        }
    }

    private static void add(SolrInputDocument doc, String name, String value) {
        if (StringUtils.isNotBlank(value)) {
            doc.addField(name, value);
        }
    }

    public void commitAll(EADCounts eadCounts) throws MalformedURLException, SolrServerException, IOException {
        if (docs.size() > 0) {
            solrTime += EadSolrServerHolder.getInstance().add(docs);
            numberOfPublishedItems += docs.size();
            LOG.debug(this.eadidstring + " #published: " + numberOfPublishedItems);
            docs = new ArrayList<SolrInputDocument>();
        }
        removeWarnings();
        if (eadCounts != null) {
            ead.setTotalNumberOfDaos(eadCounts.getNumberOfDAOsBelow());
        }
        if (eadCounts.getNumberOfDAOsBelow() == 0) {
            if (ead instanceof FindingAid) {
                FindingAid findingAid = (FindingAid) ead;
                if (EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana())) {
                    ((FindingAid) ead).setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
                }
            }
        } else if (ead instanceof FindingAid) {
            FindingAid findingAid = (FindingAid) ead;
            if (EuropeanaState.NO_EUROPEANA_CANDIDATE.equals(findingAid.getEuropeana())) {
                ((FindingAid) ead).setEuropeana(EuropeanaState.NOT_CONVERTED);
            }
        }
        if (eadCounts != null) {
            ead.setTotalNumberOfUnits(eadCounts.getNumberOfUnits());
            ead.setTotalNumberOfUnitsWithDao(eadCounts.getNumberOfUnitsWithDaosBelow());
        }
        ContentUtils.changeSearchable(ead, true);
    }

    public void rollback() throws SolrServerException, IOException {

        String solrPrefix = SolrValues.FA_PREFIX;
        if (xmlType == XmlType.EAD_HG) {
            solrPrefix = SolrValues.HG_PREFIX;
        } else if (xmlType == XmlType.EAD_SG) {
            solrPrefix = SolrValues.SG_PREFIX;
        }

        solrTime += EadSolrServerHolder.getInstance()
                .deleteByQuery(SolrFields.FOND_ID + ":" + solrPrefix + ead.getId());
        Ead rollBackEad = eadDao.findById(ead.getId(), xmlType.getClazz());
        ContentUtils.changeSearchable(rollBackEad, false);
        eadDao.store(rollBackEad);
    }

    public long getSolrTime() {
        return solrTime;
    }

}
