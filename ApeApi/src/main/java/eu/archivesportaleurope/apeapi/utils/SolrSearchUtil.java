/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.utils;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class SolrSearchUtil {

    private SolrClient solrServer;
    private SolrQuery solrQuery;
    private QueryResponse queryResponse;
    private String coreName = null;

    public SolrSearchUtil(String url) {
        this.solrServer = new HttpSolrClient(url);
    }

    public SolrSearchUtil(SolrClient solrServer) {
        this.solrServer = solrServer;
    }

    public SolrSearchUtil(String baseUrl, String coreName) {
        this(baseUrl + "/" + coreName);
    }

    public void setQuery(SolrQuery solrQuery) {
        this.solrQuery = solrQuery;
    }

    public QueryResponse getSearchResponse() throws SolrServerException, IOException {
        this.solrQuery.setHighlight(true);
        this.solrQuery.setRequestHandler("list");
        this.queryResponse = this.solrServer.query(this.coreName, this.solrQuery);
        return this.queryResponse;
    }

    public String getCoreName() {
        return coreName;
    }

    public void setCoreName(String coreName) {
        this.coreName = coreName;
    }
}
