package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.apenet.commons.types.XmlType;
import eu.archivesportaleurope.apeapi.common.datatypes.EadResponseDictionary;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.request.SearchPageRequestWithUnitId;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.PageRequest;
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.request.SortRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadHierarchyResponseSet;
import eu.archivesportaleurope.apeapi.response.hierarchy.HierarchyResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.EadSearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mahbub
 */
public class EadSearchSearviceImpl extends EadSearchService {

    private String solrUrl;
    private final String solrCore;
    private final String onlyOpenData = "openData:true";
    private final String solrAND = " AND ";
    private final String solrOR = " OR ";
    private final SolrSearchUtil searchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, String> extraParam;

    private class TypedList {

        String type;
        Map<String, Integer> keyLevel = new HashMap<>();
    }

    public EadSearchSearviceImpl(String solrUrl, String solrCore, String propFileName) {
        this.extraParam = new HashMap<>();
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public EadSearchSearviceImpl(SolrClient solrServer, String propFileName) {
        this.extraParam = new HashMap<>();
        this.solrUrl = this.solrCore = "";
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrServer);
        //ToDo: quick fix for solrj5 EmbeddedSolrServer, which is failing to use default core
        if (solrServer instanceof EmbeddedSolrServer) {
            EmbeddedSolrServer embeddedSolrServer = (EmbeddedSolrServer) solrServer;
            Collection<String> coreNames = embeddedSolrServer.getCoreContainer().getAllCoreNames();
            if (!coreNames.isEmpty()) {
                this.searchUtil.setCoreName(coreNames.iterator().next());
            }
        }
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    @Override
    public QueryResponse search(SearchRequest searchRequest, Map<String, String> extraSearchParam, boolean includeFacet) {
        List<ListFacetSettings> facetSettingsList = null;
        if (includeFacet) {
            facetSettingsList = FacetType.getDefaultEadListFacetSettings();
        }
        return this.search(searchRequest, extraSearchParam, facetSettingsList, propertiesUtil, searchUtil, new EadResponseDictionary());
    }

    @Override
    public QueryResponse searchOpenData(SearchRequest request) {
        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);
        return this.search(request, extraParam, true);
    }

    @Override
    public QueryResponse searchDocPerInstitute(InstituteDocRequest request) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(request.getCount());
        searchRequest.setStartIndex(request.getStartIndex());
        searchRequest.setSortRequest(request.getSortRequest());
        searchRequest.setQuery("ai:*" + request.getInstituteId() + this.solrAND
                + "id:" + XmlType.getTypeByResourceName(request.getDocType()).getSolrPrefix() + "*");

        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);
        return this.search(searchRequest, this.extraParam, false);
    }

    @Override
    public QueryResponse searchInstituteInGroup(int startIndex, int count) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(count);
        searchRequest.setStartIndex(startIndex);
        searchRequest.setQuery("(id:" + SolrValues.FA_PREFIX + "* OR id:" + SolrValues.HG_PREFIX + "* OR id:" + SolrValues.SG_PREFIX + "*)");

        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);

        extraParam.put("group", "true");
        extraParam.put("group.field", SolrFields.AI);
        extraParam.put("group.ngroups", "true");

        return this.search(searchRequest, extraParam, false);
    }

    private QueryResponse searchDocListByInstitute(SearchDocRequest searchRequest, String groupByFieldName, boolean includeFacet, boolean resultNeeded) {
        SearchRequest request = new SearchRequest();
        request.setFilters(searchRequest.getFilters());
        request.setDateFilters(searchRequest.getDateFilters());
        request.setQuery(searchRequest.getQuery() + this.solrAND + "type:" + searchRequest.getDocType());

        logger.info("Group query is : " + request.getQuery());
        request.setCount(searchRequest.getCount());
        request.setStartIndex(searchRequest.getStartIndex());

        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);

        extraParam.put("qf", "fond^100 title scopecontent^0.5 alterdate^0.5 other^0.1");
        extraParam.put("bq", "id:(F*)^100");
        extraParam.put("group.sort", "id " + SolrQuery.ORDER.desc);

        extraParam.put("group", "true");
        extraParam.put("group.field", groupByFieldName);
        extraParam.put("group.ngroups", "true");
        if (!resultNeeded) {
            extraParam.put("group.limit", "0");
        }

        return this.search(request, extraParam, includeFacet);
    }

    private String typeToFieldDynamicIdTranslator(String type) {
        switch (type) {
            case "fa":
                return SolrFields.FA_DYNAMIC;
            case "hg":
                return SolrFields.HG_DYNAMIC;
            case "sg":
                return SolrFields.SG_DYNAMIC;
            default:
                throw new InternalErrorException("No such type available");
        }
    }

    @Override
    public QueryResponse getEadList(SearchDocRequest searchRequest) throws InternalErrorException {
        if (null != searchRequest.getDocType()) {
            return this.searchDocListByInstitute(searchRequest, this.typeToFieldDynamicIdTranslator(searchRequest.getDocType().toLowerCase()) + "0_s", true, true);
        } else {
            throw new InternalErrorException("Invalid Search request");
        }
    }

    @Override
    public QueryResponse getDescendants(String id, SearchRequest searchRequest) {
        try {
            String levelStr = this.getDocHighestLevelText(id); //FID0_s or HID0_s etc
//            SearchRequest request = new SearchRequest();
//            request.setQuery(searchRequest.getQuery());
//            request.setCount(searchRequest.getCount());
//            request.setStartIndex(searchRequest.getStartIndex());

            extraParam.clear();
            extraParam.put("q", levelStr + ":" + id + this.solrAND + "-id:" + id + this.solrAND + this.onlyOpenData);

            return this.search(searchRequest, extraParam, true);
        } catch (SolrServerException | IOException ex) {
            throw new InternalErrorException("Solrserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public EadHierarchyResponseSet getDescendantsWithAncestors(String id, SearchRequest searchRequest) {
        try {
            String levelStr = this.getDocHighestLevelText(id); //FID0_s or HID0_s etc

            extraParam.clear();
            extraParam.put("q", levelStr + ":" + id + this.solrAND + "-id:" + id + this.solrAND + this.onlyOpenData);
            extraParam.put("fl", "*");

            QueryResponse decendentResponse = this.search(searchRequest, extraParam, true);

            SolrDocumentList decendentDocumentList = decendentResponse.getResults();
            if (decendentDocumentList.isEmpty()) {
                //No decendents!
                return new EadHierarchyResponseSet();
            }
            //keep the decendant to Ancestors list, so that we don't have to extract this info again
            Map<String, Map<String, Integer>> descendantAncesMap = new HashMap<>();
            Set<String> allAncestors = new HashSet<>();
            //for every decendent get their ancestors
            for (SolrDocument descenDocument : decendentDocumentList) {

                if (null != descenDocument && null != descenDocument.getFieldValue(SolrFields.ID)) {
                    String foundId = descenDocument.getFieldValue(SolrFields.ID).toString();
                    TypedList ancList = this.extractParentList(descenDocument, foundId);
                    descendantAncesMap.put(foundId, ancList.keyLevel);
                    ancList.keyLevel.entrySet().stream().forEach((ancId) -> {
                        allAncestors.add(ancId.getKey());
                    });
                }
            }

            StringBuilder requestStrBuffer = new StringBuilder("(");
            boolean orIt = false;
            for (String ancId : allAncestors) {
                if (orIt) {
                    requestStrBuffer.append(" OR ");
                }
                requestStrBuffer.append("id:").append(ancId);
                orIt = true;
            }

            requestStrBuffer.append(")");

            SearchRequest ancRequest = new SearchRequest();
            ancRequest.setCount(allAncestors.size());
            ancRequest.setQuery(requestStrBuffer.toString());

            QueryResponse allAncestorData = this.searchOpenData(ancRequest);
            SolrDocumentList ancDocs = allAncestorData.getResults();
            Map<String, SolrDocument> ancIdDocMap = new HashMap<>();
            for (SolrDocument ancDoc : ancDocs) {
                String ancId = ancDoc.getFieldValue(SolrFields.ID).toString();
                ancIdDocMap.put(ancId, ancDoc);
            }
            EadHierarchyResponseSet response = new EadHierarchyResponseSet(decendentResponse, descendantAncesMap, ancIdDocMap);
            return response;
        } catch (SolrServerException | IOException ex) {
            throw new InternalErrorException("Solrserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public QueryResponse getEadsByFondsUnitId(SearchPageRequestWithUnitId filteredSortedPageRequest) {
        StringBuilder query = new StringBuilder();
        query.append(SolrFields.UNITID_OF_FOND).append(":")
                .append(filteredSortedPageRequest.getFindingAidNo())
                .append(this.solrAND);
        if (StringUtils.isBlank(filteredSortedPageRequest.getUnitId())) {
            String queryToFindOnlyTopLevelItem = "(id:F* OR id:H* OR id:S*)";
            query.append(queryToFindOnlyTopLevelItem);
        } else {
            query.append("(")
                    .append(SolrFields.UNITID).append(":")
                    .append("\"")
                    .append(filteredSortedPageRequest.getFindingAidNo())
                    .append(" - ")
                    .append(filteredSortedPageRequest.getUnitId())
                    .append("\"")
                    .append(this.solrOR)
                    .append(SolrFields.UNITID).append(":")
                    .append("\"")
                    .append(filteredSortedPageRequest.getFindingAidNo())
                    .append("-")
                    .append(filteredSortedPageRequest.getUnitId())
                    .append("\"")
                    .append(")");
        }
        SearchRequest request = new SearchRequest();
        request.setCount(filteredSortedPageRequest.getCount());
        request.setStartIndex(filteredSortedPageRequest.getStartIndex());
        request.setQuery(query.toString());
        if (null != filteredSortedPageRequest.getFilters()) {
            request.setFilters(filteredSortedPageRequest.getFilters());
        }
        if (null != filteredSortedPageRequest.getDateFilters()) {
            request.setDateFilters(filteredSortedPageRequest.getDateFilters());
        }
        if (null != filteredSortedPageRequest.getSortRequest()) {
            request.setSortRequest(filteredSortedPageRequest.getSortRequest());
        }

        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);
        return this.search(request, extraParam, true);
    }

    private String getDocHighestLevelText(String id) throws SolrServerException, IOException {
        TypedList typedList = getParentList(id);
        int level = typedList.keyLevel.size();

        return typedList.type + "ID" + level + "_s";
    }

    @Override
    public QueryResponse getChildren(String id, QueryPageRequest searchRequest) {
        SearchRequest request = new SearchRequest();
        request.setCount(searchRequest.getCount());
        request.setStartIndex(searchRequest.getStartIndex());
        request.setQuery(searchRequest.getQuery() + this.solrAND + "-id:" + id + this.solrAND + "parentId:" + id);
        return this.searchOpenData(request);
    }

    @Override
    public HierarchyResponseSet getChildren(String id, PageRequest pageRequest) {
        SearchRequest request = new SearchRequest();
        request.setCount(pageRequest.getCount());
        request.setStartIndex(pageRequest.getStartIndex());
        SortRequest sortRequest = new SortRequest();
        sortRequest.addField(SolrFields.ORDER_ID);
        request.setSortRequest(sortRequest);
        request.setQuery("*" + this.solrAND + "-id:" + id + this.solrAND + "parentId:" + id);

        QueryResponse qr = this.searchOpenData(request);
        try {
            TypedList typedListParent = this.getParentList(id); //ToDo: change this, now we know numberOfAncestors
            HierarchyResponseSet hrs = new HierarchyResponseSet(qr, typedListParent.keyLevel.size() + 1);
            return hrs;
        } catch (SolrServerException | IOException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    private TypedList getParentList(String id) throws SolrServerException, IOException {
        SolrQuery query = new SolrQuery();
        query.setQuery("id:" + id + this.solrAND + onlyOpenData);
        query.setRows(1);
        query.setStart(0);
        query.setParam("fl", "id, F*_s, H*_s, S*_s, type");
        logger.debug("real query is " + query.toString());
        this.searchUtil.setQuery(query);

        QueryResponse itemResponse = this.searchUtil.getSearchResponse();

        SolrDocumentList documentList = itemResponse.getResults();
        if (documentList.isEmpty()) {
            throw new ResourceNotFoundException("No such document exist with id: " + id, "");
        }
        SolrDocument document = documentList.get(0);

        String foundId = "";
        if (null != document && null != document.getFieldValue(SolrFields.ID)) {
            foundId = document.getFieldValue(SolrFields.ID).toString();
        }

        if (!foundId.equalsIgnoreCase(id)) {
            throw new ResourceNotFoundException("No such document exist with id: " + id, "");
        }

        return this.extractParentList(document, id);
    }

    private TypedList extractParentList(SolrDocument document, String selfId) {

        String docType = document.getFieldValue(SolrFields.TYPE).toString();
        String[] typePrefix = {"", ""};
        if (docType.equals(SolrValues.FA_TYPE)) {
            typePrefix[0] = SolrFields.FA_DYNAMIC_ID;
            typePrefix[1] = SolrFields.FA_DYNAMIC;
        } else if (docType.equals(SolrValues.HG_TYPE)) {
            typePrefix[0] = SolrFields.HG_DYNAMIC_ID;
            typePrefix[1] = SolrFields.HG_DYNAMIC;
        } else if (docType.equals(SolrValues.SG_TYPE)) {
            typePrefix[0] = SolrFields.SG_DYNAMIC_ID;
            typePrefix[1] = SolrFields.SG_DYNAMIC;
        }
        TypedList typedList = new TypedList();
        typedList.type = typePrefix[1];

        int level = 0;
        while (null != document.getFieldValue(typePrefix[0] + level + "_s")) {
            typedList.keyLevel.put(document.getFieldValue(typePrefix[0] + level + "_s").toString(), level);
            level++;
        }
        if (document.getFieldValue(typePrefix[0] + (level - 1) + "_s").toString().equalsIgnoreCase(selfId)) {
            typedList.keyLevel.remove(selfId);
            level--;
        }

        return typedList;
    }

    @Override
    public HierarchyResponseSet getAncestors(String id) {
        try {
            TypedList typedList = this.getParentList(id);

            if (typedList.keyLevel.isEmpty()) {
                return new HierarchyResponseSet();
            }

            StringBuilder requestStrBuffer = new StringBuilder("(");
            boolean orIt = false;
            for (Map.Entry<String, Integer> aLevel : typedList.keyLevel.entrySet()) {
                if (orIt) {
                    requestStrBuffer.append(" OR ");
                }
                requestStrBuffer.append("id:").append(aLevel.getKey());
                orIt = true;
            }

            requestStrBuffer.append(")");

            SearchRequest request = new SearchRequest();
//            request.setCount(pageRequest.getCount());
            request.setCount(typedList.keyLevel.size());
//            request.setStartIndex(pageRequest.getStartIndex());
            request.setQuery(requestStrBuffer.toString());

            QueryResponse qr = this.searchOpenData(request);
            HierarchyResponseSet hrs = new HierarchyResponseSet(qr, typedList.keyLevel);
            return hrs;

        } catch (SolrServerException | IOException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }
}
