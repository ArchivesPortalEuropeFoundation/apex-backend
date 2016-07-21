/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.apenet.commons.solr.SolrQueryBuilder;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerResponseDictionary;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.DateFilterRequest;
import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchFilterRequest;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadDocResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.text.ParseException;
import java.util.List;
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

    public abstract QueryResponse search(SearchRequest request, String extraSearchParam, boolean includeFacet);

    public abstract QueryResponse searchOpenData(SearchRequest request);

    public abstract QueryResponse searchDocPerInstitute(InstituteDocRequest request);

    public abstract QueryResponse searchInstituteInGroup(int startIndex, int count);

    public abstract QueryResponse getEadList(SearchDocRequest searchRequest);

    private final SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected QueryResponse search(SearchRequest searchRequest, String extraSearchParam,
            List<ListFacetSettings> facetSettingsList, PropertiesUtil propertiesUtil, SolrSearchUtil eadSearchUtil) {
        try {
            String extraParam = "";
            if (extraSearchParam != null) {
                extraParam = extraSearchParam;
            }
            SolrQuery query = queryBuilder.getListViewQuery(searchRequest.getStartIndex(), facetSettingsList, null, null, null, true);

            for (SearchFilterRequest searchFilter : searchRequest.getFilters()) {
                queryBuilder.addFilters(query,
                        FacetType.getFacetByName(ServerResponseDictionary.getSolrFieldName(searchFilter.getFacetFieldName())),
                        searchFilter.getFacetFieldIds());
            }

            for (DateFilterRequest dateFilter : searchRequest.getDateFilters()) {
                if (dateFilter.getDateFieldName().equalsIgnoreCase("fromDate")) {
                    queryBuilder.addFromDateFilter(query, dateFilter.getDateFieldId());
                } else if (dateFilter.getDateFieldName().equalsIgnoreCase("toDate")) {
                    queryBuilder.addToDateFilter(query, dateFilter.getDateFieldId());
                }
            }
            query.setQuery(searchRequest.getQuery() + extraParam);

            if (searchRequest.getCount() <= 0) {
                logger.info(":::Default Count vale from prop is : " + propertiesUtil.getValueFromKey("search.request.default.count"));
                query.setRows(Integer.parseInt(propertiesUtil.getValueFromKey("search.request.default.count")));
            } else {
                query.setRows(searchRequest.getCount());
            }
            logger.debug("Final search query: " + query.getFields());
            eadSearchUtil.setQuery(query);

            return eadSearchUtil.getSearchResponse();
        } catch (SolrServerException | ParseException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }
}
