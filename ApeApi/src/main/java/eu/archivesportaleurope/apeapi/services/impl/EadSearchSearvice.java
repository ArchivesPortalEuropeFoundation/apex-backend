package eu.archivesportaleurope.apeapi.services.impl;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author Mahbub
 */
public class EadSearchSearvice implements SearchService {
    private String solrUrl;
    private final String solrCore;
    private final SolrSearchUtil eadSearchUtil;

    private EadSearchSearvice(String solrUrl, String solrCore) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        this.eadSearchUtil = new SolrSearchUtil(solrUrl, solrCore);
    }

    private EadSearchSearvice(SolrServer solrServer) {
        this.solrUrl = this.solrCore = "";
        this.eadSearchUtil = new SolrSearchUtil(solrServer);
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    @Override
    public EadResponseSet search(SearchRequest searchRequest, String extraSearchParam) throws SolrServerException {
        if (extraSearchParam==null) {
            extraSearchParam = "";
        }
        SolrQuery query = new SolrQuery(searchRequest.getQuery() + extraSearchParam);
        query.setStart(searchRequest.getStart());
        if (searchRequest.getCount() <= 0) {
            query.setRows(10);
        } else {
            query.setRows(searchRequest.getCount());
        }

        this.eadSearchUtil.setQuery(query);

        QueryResponse response = this.eadSearchUtil.getSearchResponse();
        return new EadResponseSet(response);
    }
    
    @Override
    public EadResponseSet searchOpenData(SearchRequest request) throws SolrServerException {
        return this.search(request, " AND openData:true");
    }
}
