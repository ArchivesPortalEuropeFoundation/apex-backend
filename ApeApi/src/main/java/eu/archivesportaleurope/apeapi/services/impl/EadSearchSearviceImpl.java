package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.apenet.commons.types.XmlType;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.EadSearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.List;
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
    private final String onlyOpenData = " openData:true ";
    private final SolrSearchUtil searchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        try {
            List<ListFacetSettings> facetSettingsList = null;
            if (includeFacet) {
                facetSettingsList = FacetType.getDefaultEadListFacetSettings();
            }
            return this.search(searchRequest, extraSearchParam, facetSettingsList, propertiesUtil, searchUtil);

        } catch (InternalErrorException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
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
        searchRequest.setQuery(this.onlyOpenData + "AND ai:*" + request.getInstituteId()
                + " AND id:" + XmlType.getTypeByResourceName(request.getDocType()).getSolrPrefix() + "*");
        return this.search(searchRequest, "", false);
    }

    @Override
    public QueryResponse searchInstituteInGroup(int startIndex, int count) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(count);
        searchRequest.setStartIndex(startIndex);
        searchRequest.setQuery("(id:" + SolrValues.FA_PREFIX + "* OR id:" + SolrValues.HG_PREFIX + "* OR id:" + SolrValues.SG_PREFIX + "*)" + " AND" + this.onlyOpenData);
        return this.groupByQuery(searchRequest, SolrFields.AI, true);
    }

    private QueryResponse groupByQueryOpenData(SearchDocRequest searchRequest, String groupByFieldName, boolean resultNeeded) {
        SearchRequest request = new SearchRequest();
//        if (searchRequest.getLevel() > 0 && searchRequest.getParentId() != null) {
//            request.setQuery(searchRequest.getQuery()
//                    + " AND type:" + searchRequest.getDocType()
//                    + " AND " + this.typeToFieldDynamicIdTranslator(searchRequest.getDocType()) + (searchRequest.getLevel() - 1) + "_s:" + searchRequest.getParentId()
//                    + " AND openData:true");
//        } else {
        request.setQuery(searchRequest.getQuery() + " AND type:" + searchRequest.getDocType() + " AND" + this.onlyOpenData);
//        }
        logger.info("Group query is : " + request.getQuery());
        request.setCount(searchRequest.getCount());
        request.setStartIndex(searchRequest.getStartIndex());
        return this.groupByQuery(request, groupByFieldName, resultNeeded);
    }

    private String typeToFieldDynamicIdTranslator(String type) {
        switch (type) {
            case "fa":
                return SolrFields.FA_DYNAMIC_ID;
            case "hg":
                return SolrFields.HG_DYNAMIC_ID;
            case "sg":
                return SolrFields.SG_DYNAMIC_ID;
        }
        return "";
    }

    private QueryResponse groupByQuery(SearchRequest searchRequest, String groupByFieldName, boolean resultNeeded) {
        try {
            SolrQuery query = new SolrQuery();
            query.setQuery(searchRequest.getQuery() + " AND "+onlyOpenData);
            query.add("group", "true");
            query.add("group.field", groupByFieldName);
            query.add("group.ngroups", "true");
            if (!resultNeeded) {
                query.add("group.limit", "0");
            }
            query.setStart(searchRequest.getStartIndex());
            if (searchRequest.getCount() > 0) {
                query.setRows(searchRequest.getCount());
            } else {
                query.setRows(Integer.valueOf(propertiesUtil.getValueFromKey("search.request.default.count")));
            }
            query.setParam("fl", "country,repositoryCode");
            query.setSort("orderId", SolrQuery.ORDER.asc);
            logger.debug("real query is " + query.toString());
            this.searchUtil.setQuery(query);

            return this.searchUtil.getSearchResponse();
        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solrserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public QueryResponse getEadList(SearchDocRequest searchRequest) throws InternalErrorException {
        if (null != searchRequest.getDocType()) {
            if (searchRequest.getCount() <= 0) {
                searchRequest.setCount(Integer.valueOf(propertiesUtil.getValueFromKey("search.request.default.count")));
            }
            switch (searchRequest.getDocType().toLowerCase()) {
                case "fa":
                    return this.groupByQueryOpenData(searchRequest, SolrFields.FA_DYNAMIC + "0_s", false);
                case "hg":
                    return this.groupByQueryOpenData(searchRequest, SolrFields.HG_DYNAMIC + "0_s", false);
                case "sg":
                    return this.groupByQueryOpenData(searchRequest, SolrFields.SG_DYNAMIC + "0_s", false);
                default:
                    throw new InternalErrorException("No such type available");
            }
        } else {
            throw new InternalErrorException("Invalid Search request");
        }
    }

    @Override
    public QueryResponse getDescendants(String id, QueryPageRequest searchRequest) {
        try {
            String[] levelStr = this.getDocHighestLevel(id); //FID0_s or HID0_s etc
            SearchRequest request = new SearchRequest();
            request.setQuery(searchRequest.getQuery());
            request.setCount(searchRequest.getCount());
            request.setStartIndex(searchRequest.getStartIndex());
            return this.search(request, levelStr[0] + ":" + id + " AND" + this.onlyOpenData, false);
        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solrserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    private String[] getDocHighestLevel(String id) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("id:" + id + " AND "+onlyOpenData);
        query.setRows(1);
        query.setStart(0);
        query.setParam("fl", "id, F*_s, type");
        logger.debug("real query is " + query.toString());
        this.searchUtil.setQuery(query);

        QueryResponse itemResponse = this.searchUtil.getSearchResponse();
        
        SolrDocumentList documentList = itemResponse.getResults();
        String foundId = "";
        SolrDocument document = documentList.get(0);
        if (null != document && null != document.getFieldValue(SolrFields.ID)) {
            foundId = document.getFieldValue(SolrFields.ID).toString();
        }

        if (!foundId.equalsIgnoreCase(id)) {
            throw new ResourceNotFoundException("No such document exist with id: " + id, "");
        }
        String docType = document.getFieldValue(SolrFields.TYPE).toString();
        String[] res = {"", ""};
        if (docType.equals(SolrValues.FA_TYPE)) {
            res[0] = SolrFields.FA_DYNAMIC_ID;
            res[1] = SolrFields.FA_DYNAMIC;
        } else if (docType.equals(SolrValues.HG_TYPE)) {
            res[0] = SolrFields.HG_DYNAMIC_ID;
            res[1] = SolrFields.HG_DYNAMIC;
        } else if (docType.equals(SolrValues.SG_TYPE)) {
            res[0] = SolrFields.SG_DYNAMIC_ID;
            res[1] = SolrFields.SG_DYNAMIC;
        }
        int level = 0;
        while (null != document.getFieldValue(res[0] + level + "_s")) {
            level++;
        }
        if (document.getFieldValue(res[0]+ (level-1) + "_s").toString().equalsIgnoreCase(id)) {
            level--;
        }
        res[0] = res[0] + level + "_s";
        res[1] = res[1] + level + "_s";
        return res;
    }

    @Override
    public QueryResponse getChildren(String id, QueryPageRequest searchRequest) {
        SearchRequest request = new SearchRequest();
        request.setCount(searchRequest.getCount());
        request.setStartIndex(searchRequest.getStartIndex());
        request.setQuery(searchRequest.getQuery() + " AND parentId:" + id);
        return this.searchOpenData(request);
    }
}
