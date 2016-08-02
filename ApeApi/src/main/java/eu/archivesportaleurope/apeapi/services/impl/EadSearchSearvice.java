package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.apenet.commons.types.XmlType;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mahbub
 */
public class EadSearchSearvice extends SearchService {
    
    private String solrUrl;
    private final String solrCore;
    private final SolrSearchUtil searchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public EadSearchSearvice(String solrUrl, String solrCore, String propFileName) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }
    
    public EadSearchSearvice(SolrServer solrServer, String propFileName) {
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
        return this.search(request, " AND openData:true", true);
    }
    
    @Override
    public QueryResponse searchDocPerInstitute(InstituteDocRequest request) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(request.getCount());
        searchRequest.setStartIndex(request.getStartIndex());
        searchRequest.setQuery("openData:true AND ai:*" + request.getInstituteId()
                + " AND id:" + XmlType.getTypeByResourceName(request.getDocType()).getSolrPrefix() + "*");
        return this.search(searchRequest, "", false);
    }
    
    @Override
    public QueryResponse searchInstituteInGroup(int startIndex, int count) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setCount(count);
        searchRequest.setStartIndex(startIndex);
        searchRequest.setQuery("(id:" + SolrValues.FA_PREFIX + "* OR id:" + SolrValues.HG_PREFIX + "* OR id:" + SolrValues.SG_PREFIX + "*)" + " AND openData:true");
        return this.groupByQuery(searchRequest, SolrFields.AI, true);
    }
    
    private QueryResponse groupByQueryOpenData(SearchDocRequest searchRequest, String groupByFieldName, boolean resultNeeded) {
        SearchRequest request = new SearchRequest();
        if (searchRequest.getLevel() > 0 && searchRequest.getParentId() != null) {
            request.setQuery(searchRequest.getQuery()
                    + " AND type:" + searchRequest.getDocType()
                    + " AND " + this.typeToFieldDynamicIdTranslator(searchRequest.getDocType()) + (searchRequest.getLevel() - 1) + "_s:" + searchRequest.getParentId()
                    + " AND openData:true");
        } else {
            request.setQuery(searchRequest.getQuery() + " AND type:" + searchRequest.getDocType() + " AND openData:true");
        }
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
            query.setQuery(searchRequest.getQuery());
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
            switch (searchRequest.getDocType()) {
                case "fa":
                    return this.groupByQueryOpenData(searchRequest, SolrFields.FA_DYNAMIC + searchRequest.getLevel() + "_s", false);
                case "hg":
                    return this.groupByQueryOpenData(searchRequest, SolrFields.HG_DYNAMIC + searchRequest.getLevel() + "_s", false);
                case "sg":
                    return this.groupByQueryOpenData(searchRequest, SolrFields.SG_DYNAMIC + searchRequest.getLevel() + "_s", false);
                default:
                    throw new InternalErrorException("No such type available");
            }
        } else {
            throw new InternalErrorException("Invalid Search request");
        }
    }
}
