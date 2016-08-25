/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import io.swagger.annotations.ApiModelProperty;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author kaisar
 */
public class SearchStatResponse {

    @ApiModelProperty(value = "Required time to execute query in ms")
    private int queryTime;
    @ApiModelProperty(value = "Number of rows found")
    private int rows;
    @ApiModelProperty(value = "Search Query")
    private String q;

    public SearchStatResponse(QueryResponse response) {
        this.queryTime = response.getQTime();
        Object row = ((NamedList) response.getResponseHeader().get("params")).get("rows");
        if (row != null) {
            this.rows = Integer.parseInt(row.toString());
        } else {
            this.rows = 0;
        }
        this.q = ((NamedList) response.getResponseHeader().get("params")).get("q").toString();
    }

    /**
     * Default constructor
     */
    public SearchStatResponse() {
        this.q = "";
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
