package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.apenet.commons.types.XmlType;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.PageRequest;
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.request.SortRequest;
import eu.archivesportaleurope.apeapi.response.hierarchy.HierarchyResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.EadSearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
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
    private final SolrSearchUtil searchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private class TypedList {

        String type;
        Map<String, Integer> keyLevel = new HashMap<>();
    }

    public EadSearchSearviceImpl(String solrUrl, String solrCore, String propFileName) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public EadSearchSearviceImpl(SolrServer solrServer, String propFileName) {
        this.solrUrl = this.solrCore = "";
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrServer);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    @Override
    public QueryResponse search(SearchRequest searchRequest, String extraSearchParam, boolean includeFacet) {
        List<ListFacetSettings> facetSettingsList = null;
        if (includeFacet) {
            facetSettingsList = FacetType.getDefaultEadListFacetSettings();
        }
        return this.search(searchRequest, extraSearchParam, facetSettingsList, propertiesUtil, searchUtil);
    }

    @Override
    public QueryResponse searchOpenData(SearchRequest request) {
        return this.search(request, this.onlyOpenData, true);
    }

    @Override
    public QueryResponse searchDocPerInstitute(InstituteDocRequest request) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(request.getCount());
        searchRequest.setStartIndex(request.getStartIndex());
        searchRequest.setQuery("ai:*" + request.getInstituteId() + this.solrAND
                + "id:" + XmlType.getTypeByResourceName(request.getDocType()).getSolrPrefix() + "*");
        return this.search(searchRequest, this.onlyOpenData, false);
    }

    @Override
    public QueryResponse searchInstituteInGroup(int startIndex, int count) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(count);
        searchRequest.setStartIndex(startIndex);
        searchRequest.setQuery("(id:" + SolrValues.FA_PREFIX + "* OR id:" + SolrValues.HG_PREFIX + "* OR id:" + SolrValues.SG_PREFIX + "*)");
        return this.groupByQuery(searchRequest, SolrFields.AI, false, true);
    }

    private QueryResponse groupByQueryOpenData(SearchDocRequest searchRequest, String groupByFieldName, boolean includeFacet, boolean resultNeeded) {
        SearchRequest request = new SearchRequest();
        request.setFilters(searchRequest.getFilters());
        request.setDateFilters(searchRequest.getDateFilters());
        request.setQuery(searchRequest.getQuery() + this.solrAND + "type:" + searchRequest.getDocType());

        logger.info("Group query is : " + request.getQuery());
        request.setCount(searchRequest.getCount());
        request.setStartIndex(searchRequest.getStartIndex());
        return this.groupByQuery(request, groupByFieldName, includeFacet, resultNeeded);
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

    private QueryResponse groupByQuery(SearchRequest searchRequest, String groupByFieldName, boolean includeFacet, boolean resultNeeded) {
        List<ListFacetSettings> facetSettingsList = null;
        if (includeFacet) {
            facetSettingsList = FacetType.getDefaultEadListFacetSettings();
        }
        return super.groupByQuery(searchRequest, this.onlyOpenData, facetSettingsList, propertiesUtil, searchUtil, groupByFieldName, resultNeeded);
    }

    @Override
    public QueryResponse getEadList(SearchDocRequest searchRequest) throws InternalErrorException {
        if (null != searchRequest.getDocType()) {
            if (searchRequest.getCount() <= 0) {
                searchRequest.setCount(Integer.valueOf(propertiesUtil.getValueFromKey("search.request.default.count")));
            }
            return this.groupByQueryOpenData(searchRequest, this.typeToFieldDynamicIdTranslator(searchRequest.getDocType().toLowerCase()) + "0_s", true, true);
        } else {
            throw new InternalErrorException("Invalid Search request");
        }
    }

    @Override
    public QueryResponse getDescendants(String id, QueryPageRequest searchRequest) {
        try {
            String levelStr = this.getDocHighestLevelText(id); //FID0_s or HID0_s etc
            SearchRequest request = new SearchRequest();
            request.setQuery(searchRequest.getQuery());
            request.setCount(searchRequest.getCount());
            request.setStartIndex(searchRequest.getStartIndex());
            return this.search(request, levelStr + ":" + id + this.solrAND + "-id:" + id + this.solrAND + this.onlyOpenData, false);
        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solrserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    private String getDocHighestLevelText(String id) throws SolrServerException {
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
            TypedList typedListParent = this.getParentList(id);
            //the size of current id's parent list (which is the parent of it's children) indecate it's level
//            TypedList typedListChildren = new TypedList();
//            for (SolrDocument document : qr.getResults()) {
//                typedListChildren.keyLevel.put(document.getFieldValue(SolrFields.ID).toString(), typedListParent.keyLevel.size()+1);
//            }
            HierarchyResponseSet hrs = new HierarchyResponseSet(qr, typedListParent.keyLevel.size() + 1);
            return hrs;
        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    private TypedList getParentList(String id) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("id:" + id + this.solrAND + onlyOpenData);
        query.setRows(1);
        query.setStart(0);
        query.setParam("fl", "id, F*_s, type");
        logger.debug("real query is " + query.toString());
        this.searchUtil.setQuery(query);

        QueryResponse itemResponse = this.searchUtil.getSearchResponse();

        SolrDocumentList documentList = itemResponse.getResults();
        String foundId = "";
        if (documentList.isEmpty()) {
            throw new ResourceNotFoundException("No such document exist with id: " + id, "");
        }
        SolrDocument document = documentList.get(0);
        if (null != document && null != document.getFieldValue(SolrFields.ID)) {
            foundId = document.getFieldValue(SolrFields.ID).toString();
        }

        if (!foundId.equalsIgnoreCase(id)) {
            throw new ResourceNotFoundException("No such document exist with id: " + id, "");
        }
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
        if (document.getFieldValue(typePrefix[0] + (level - 1) + "_s").toString().equalsIgnoreCase(id)) {
            typedList.keyLevel.remove(id);
            level--;
        }

        return typedList;
    }

    @Override
    public HierarchyResponseSet getAncestors(String id) {
        try {
            TypedList typedList = this.getParentList(id);
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

            if (typedList.keyLevel.isEmpty()) {
                return new HierarchyResponseSet();
            } else {
                QueryResponse qr = this.searchOpenData(request);
                HierarchyResponseSet hrs = new HierarchyResponseSet(qr, typedList.keyLevel);
                return hrs;
            }

        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }
}
