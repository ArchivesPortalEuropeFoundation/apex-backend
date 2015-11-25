/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.backend.apeapi.entity.ead;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;

/**
 *
 * @author kaisar
 */
class ResponseHeader {

    private int status;
    private int QTtime;
    private int rows;
    private String q;

    public ResponseHeader(QueryResponse response) {
        this.status = response.getStatus();
        this.QTtime = response.getQTime();
        this.rows = Integer.parseInt(((NamedList) response.getResponseHeader().get("params")).get("rows").toString());
        this.q = ((NamedList) response.getResponseHeader().get("params")).get("q").toString();
    }

    public ResponseHeader() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getQTtime() {
        return QTtime;
    }

    public void setQTtime(int QTtime) {
        this.QTtime = QTtime;
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
