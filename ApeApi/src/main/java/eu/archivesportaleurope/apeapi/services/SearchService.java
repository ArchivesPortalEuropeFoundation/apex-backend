/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.apenet.commons.solr.SolrQueryBuilder;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.EadResponseDictionary;
import eu.archivesportaleurope.apeapi.common.datatypes.SolrApiResponseDictionary;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.DateFilterRequest;
import eu.archivesportaleurope.apeapi.request.SearchFilterRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.request.SortRequest;
import eu.archivesportaleurope.apeapi.response.common.SortFields;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Any search service will have to implement this interface.
 *
 * @author Mahbub
 */
public abstract class SearchService {
    private final String solrAND = " AND ";

    public abstract QueryResponse search(SearchRequest request, Map<String, String> extraSearchParam, boolean includeFacet);

    public abstract QueryResponse searchOpenData(SearchRequest request);

    private final SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected QueryResponse search(SearchRequest searchRequest, Map<String, String> extraSearchParam,
            List<ListFacetSettings> facetSettingsList, PropertiesUtil propertiesUtil, SolrSearchUtil eadSearchUtil,
            SolrApiResponseDictionary dictionary) {
        try {

            SolrQuery query = this.getFacatedQuery(searchRequest, facetSettingsList, dictionary);

            if (searchRequest.getCount() <= 0) {
                logger.info(":::Default Count value from prop is : " + propertiesUtil.getValueFromKey("search.request.default.count"));
                query.setRows(Integer.parseInt(propertiesUtil.getValueFromKey("search.request.default.count")));
            } else {
                query.setRows(searchRequest.getCount());
            }
            
            String queryStr = searchRequest.getQuery();
            
            if (extraSearchParam != null && !extraSearchParam.isEmpty()) {
                for (Map.Entry<String, String> entry : extraSearchParam.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase("q") && !entry.getValue().isEmpty()) {
                        queryStr += solrAND + entry.getValue();
                    } else {
                        query.add(entry.getKey(), entry.getValue());
                    }
                }
            }
            query.setQuery(queryStr);

            //openData - true or false should be managed by the query
            assert (query.getQuery().contains("openData") == true);

            logger.debug("Final search query: " + query);
            eadSearchUtil.setQuery(query);

            return eadSearchUtil.getSearchResponse();
        } catch (SolrServerException | ParseException | IOException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }
    
    private SolrQuery getFacatedQuery(SearchRequest searchRequest,
            List<ListFacetSettings> facetSettingsList, SolrApiResponseDictionary dictionary) throws SolrServerException, ParseException {
        SolrQuery query = queryBuilder.getListViewQuery(searchRequest.getStartIndex(), facetSettingsList, null, null, true);

        for (SearchFilterRequest searchFilter : searchRequest.getFilters()) {
            queryBuilder.addFilters(query,
                    FacetType.getFacetByName(dictionary.getSolrFieldName(searchFilter.getFacetFieldName())),
                    searchFilter.getFacetFieldIds());
        }

        for (DateFilterRequest dateFilter : searchRequest.getDateFilters()) {
            queryBuilder.addDateFilter(query, FacetType.getFacetByName(dictionary.getSolrFieldName(dateFilter.getDateFieldName())),
                    dateFilter.getDateFieldId());
        }

        SortRequest sortFilterRequest = searchRequest.getSortRequest();
        SolrQuery.ORDER order = SolrQuery.ORDER.asc;
        if (sortFilterRequest != null) {
            if ("desc".equals(sortFilterRequest.getSortType())) {
                order = SolrQuery.ORDER.desc;
            }
            if (sortFilterRequest.getFields() != null) {
                //ToDo: For every search type, sort fileds could be different
                //If we don't want exception because sort field was not found in specific type
                //this should be fixed
                Map<String, String> sortFieldMap = new SortFields().getSolrSortFieldMap();
                for (String field : sortFilterRequest.getFields()) {
                    String solrSortField = sortFieldMap.get(field);
                    if (solrSortField != null) {
                        query.addSort(solrSortField, order);
                    }
                }
            }
        }
        return query;
    }
}
