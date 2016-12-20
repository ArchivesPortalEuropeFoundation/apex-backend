/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrQueryBuilder;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.Ead3SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.List;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class Ead3SearchServiceImpl extends Ead3SearchService {

    private String solrUrl;
    private final String solrCore;
    private final String onlyOpenData = "openData:true";
    private final String solrAND = " AND ";
    private final SolrSearchUtil searchUtil;
    private final PropertiesUtil propertiesUtil;
    private final SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Ead3SearchServiceImpl(String solrUrl, String solrCore, String propFileName) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public Ead3SearchServiceImpl(SolrServer solrServer, String propFileName) {
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
    public QueryResponse search(SearchRequest request, String extraSearchParam, boolean includeFacet) {
        List<ListFacetSettings> facetSettingsList = null;
        if (includeFacet) {
            facetSettingsList = FacetType.getDefaultEad3ListFacetSettings();
        }
        return this.search(request, extraSearchParam, facetSettingsList, this.propertiesUtil, this.searchUtil);
    }

//    @Override
//    protected QueryResponse search(SearchRequest searchRequest, String extraSearchParam,
//            List<ListFacetSettings> facetSettingsList, PropertiesUtil propertiesUtil, SolrSearchUtil eadSearchUtil) {
//        try {
//
//            SolrQuery query = this.getFacatedQuery(searchRequest, facetSettingsList);
//
//            if (extraSearchParam != null && !"".equals(extraSearchParam)) {
//                query.setQuery(searchRequest.getQuery() + " AND " + extraSearchParam);
//            } else {
//                query.setQuery(searchRequest.getQuery());
//            }
//
//            if (searchRequest.getCount() <= 0) {
//                logger.info(":::Default Count vale from prop is : " + propertiesUtil.getValueFromKey("search.request.default.count"));
//                query.setRows(Integer.parseInt(propertiesUtil.getValueFromKey("search.request.default.count")));
//            } else {
//                query.setRows(searchRequest.getCount());
//            }
//
//            //openData - true or false should be managed by the query
//            assert (query.getQuery().contains("openData") == true);
//
//            logger.debug("Final search query: " + query);
//            eadSearchUtil.setQuery(query);
//
//            return eadSearchUtil.getSearchResponse();
//        } catch (SolrServerException | ParseException ex) {
//            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
//        }
//    }
//
//    private SolrQuery getFacatedQuery(SearchRequest searchRequest,
//            List<ListFacetSettings> facetSettingsList) throws SolrServerException, ParseException {
//        SolrQuery query = queryBuilder.getEad3ListViewQuery(searchRequest.getStartIndex(), facetSettingsList, null, null, null, true);
//
//        for (SearchFilterRequest searchFilter : searchRequest.getFilters()) {
//            queryBuilder.addFilters(query,
//                    FacetType.getFacetByName(ServerResponseDictionary.getSolrFieldName(searchFilter.getFacetFieldName())),
//                    searchFilter.getFacetFieldIds());
//        }
//
//        for (DateFilterRequest dateFilter : searchRequest.getDateFilters()) {
//            if (dateFilter.getDateFieldName().equalsIgnoreCase("fromDate")) {
//                queryBuilder.addFromDateFilter(query, dateFilter.getDateFieldId());
//            } else if (dateFilter.getDateFieldName().equalsIgnoreCase("toDate")) {
//                queryBuilder.addToDateFilter(query, dateFilter.getDateFieldId());
//            }
//        }
//
//        SortRequest sortFilterRequest = searchRequest.getSortRequest();
//        SolrQuery.ORDER order = SolrQuery.ORDER.asc;
//        if (sortFilterRequest != null) {
//            if ("desc".equals(sortFilterRequest.getSortType())) {
//                order = SolrQuery.ORDER.desc;
//            }
//            if (sortFilterRequest.getFields() != null) {
//                //ToDo: For every search type, sort fileds could be different
//                //If we don't want exception because sort field was not found in specific type
//                //this should be fixed
//                Map<String, String> sortFieldMap = new SortFields().getSolrSortFieldMap();
//                for (String field : sortFilterRequest.getFields()) {
//                    String solrSortField = sortFieldMap.get(field);
//                    if (solrSortField != null) {
//                        query.addSort(solrSortField, order);
//                    }
//                }
//            }
//        }
//        return query;
//    }
    @Override
    public QueryResponse searchOpenData(SearchRequest request) {
        return this.search(request, this.onlyOpenData, false);
    }

}
