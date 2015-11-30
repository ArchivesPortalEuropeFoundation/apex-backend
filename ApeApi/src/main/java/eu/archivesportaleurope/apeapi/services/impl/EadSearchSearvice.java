package eu.archivesportaleurope.apeapi.services.impl;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author Mahbub
 */
public class EadSearchSearvice implements SearchService {

    private final String solrUrl;
    private final String solrCore;
    private final SolrSearchUtil eadSearchUtil;

    private EadSearchSearvice(String solrUrl, String solrCore) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        this.eadSearchUtil = new SolrSearchUtil(this.solrUrl, this.solrCore);
    }

    @Override
    public QueryResponse search(SearchRequest searchRequest) throws SolrServerException {
        SolrQuery query = new SolrQuery(searchRequest.getQuery());
        query.setStart(searchRequest.getStart());
        query.setRows(searchRequest.getCount());

        this.eadSearchUtil.setQuery(query);
        
        QueryResponse response = this.eadSearchUtil.getSearchResponse();
        return response;
    }
}
