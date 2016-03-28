/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author kaisar
 */
public class SearchStatResponse {
    private int queryTime;
    private int rows;
    private String q;

    public SearchStatResponse(QueryResponse response) {
        this.queryTime = response.getQTime();
        this.rows = Integer.parseInt(((NamedList) response.getResponseHeader().get("params")).get("rows").toString());
        this.q = ((NamedList) response.getResponseHeader().get("params")).get("q").toString();
    }

    public SearchStatResponse() {
    }

    public int getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(int queryTime) {
        this.queryTime = queryTime;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}