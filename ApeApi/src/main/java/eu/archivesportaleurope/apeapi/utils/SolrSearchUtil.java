/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.utils;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;

/**
 *
 * @author kaisar
 */
public class SolrSearchUtil {

    private SolrServer solrServer;
    private SolrQuery solrQuery;
    private QueryResponse queryResponse;

    public SolrSearchUtil(String url) {
        this.solrServer = new HttpSolrServer(url);
    }

    public SolrSearchUtil(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
    
    public SolrSearchUtil(String baseUrl, String coreName) {
        this(baseUrl + "/" + coreName);
    }

    public void setQuery(SolrQuery solrQuery) {
        this.solrQuery = solrQuery;
    }

    public QueryResponse getSearchResponse() throws SolrServerException {
        this.solrQuery.setHighlight(true);
        this.solrQuery.setRequestHandler("list");
        this.queryResponse = this.solrServer.query(this.solrQuery);
        return this.queryResponse;
    }

    public TermsResponse getTermsResponse(String q) throws SolrServerException {
        SolrQuery query = new SolrQuery(q);
        query.setRequestHandler("list");
        query.set("spellcheck", "true");
        query.setTermsPrefix(q.toLowerCase());
        query.setTermsLower(q.toLowerCase());
        query.setRequestHandler("/terms");
        this.queryResponse = this.solrServer.query(query);
        return this.queryResponse.getTermsResponse();
    }

    public QueryResponse getSuggestion() throws SolrServerException {
        solrQuery.setRequestHandler("list");
        solrQuery.set("spellcheck", "true");
        return solrServer.query(solrQuery);
    }
}
