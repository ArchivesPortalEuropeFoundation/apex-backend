package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.apenet.commons.types.XmlType;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadDocResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadDocResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
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
        try {
            SolrQuery query = new SolrQuery();
            query.setQuery("(id:" + SolrValues.FA_PREFIX + "* OR id:" + SolrValues.HG_PREFIX + "* OR id:" + SolrValues.SG_PREFIX + "*)" + " AND openData:true");
            query.add("group", "true");
            query.add("group.field", "ai");
            query.add("group.query", "true");
            query.add("group.ngroups", "true");
            query.setStart(startIndex);
            query.setRows(count);
            query.setParam("fl", "country,repositoryCode");
            query.setSort("orderId", SolrQuery.ORDER.asc);
            logger.debug("real query is " + query.toString());
            this.searchUtil.setQuery(query);

            return this.searchUtil.getSearchResponse();
        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    private QueryResponse getFonds(SearchDocRequest searchRequest) {
        try {
            SolrQuery queryContext = new SolrQuery(searchRequest.getQuery());
            if (searchRequest.getDocType().equalsIgnoreCase("fa")) {
                queryContext.addFacetField(SolrFields.FA_DYNAMIC_NAME);
            } else if (searchRequest.getDocType().equalsIgnoreCase("hg")) {
                queryContext.addFacetField(SolrFields.HG_DYNAMIC_NAME);
            } else if (searchRequest.getDocType().equalsIgnoreCase("sg")) {
                queryContext.addFacetField(SolrFields.SG_DYNAMIC_NAME);
            } else {
                throw new ResourceNotFoundException("Given type is unknown: "+searchRequest.getDocType(), "Unknow docType: "+searchRequest.getDocType());
            }
            
            queryContext.setFacetSort(FacetParams.FACET_SORT_COUNT);
            queryContext.setParam("facet.offset", searchRequest.getStartIndex() + "");
            if (searchRequest.getCount() <= 0) {
                queryContext.setFacetLimit(Integer.valueOf(propertiesUtil.getValueFromKey("search.request.default.count")));
            } else {
                queryContext.setFacetLimit(searchRequest.getCount());
            }
            
            queryContext.setFacetMinCount(1);
            queryContext.setParam("facet.numTerms", "true");
            queryContext.setParam("openData", "true");
            queryContext.setRows(0);
            queryContext.setHighlight(false);
            queryContext.setParam("facet.method", "enum");
            queryContext.setRequestHandler("context");
            this.searchUtil.setQuery(queryContext);
            return this.searchUtil.getSearchResponse();
        } catch (SolrServerException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public EadDocResponseSet getEadList(SearchDocRequest searchRequest) {
        List<FacetField.Count> counts = new ArrayList<>();
        QueryResponse queryResponse = this.getFonds(searchRequest);
        
        int totalRes = 0;
        for (FacetField facetField : queryResponse.getFacetFields()) {
            List<FacetField.Count> faCounts = facetField.getValues();
            totalRes += facetField.getValueCount();
            if (faCounts != null) {
                counts.addAll(faCounts);
            }
        }

        searchRequest.setCount((int) queryResponse.getResults().getNumFound());
        logger.info(":::: " + queryResponse.getResults().getNumFound());
        return new EadDocResponseSet(searchRequest, counts, totalRes, EadDocResponse.Type.FOND);
    }
}
