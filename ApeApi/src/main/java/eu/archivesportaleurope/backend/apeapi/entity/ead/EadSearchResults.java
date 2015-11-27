/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.backend.apeapi.entity.ead;

import eu.archivesportaleurope.backend.apeapi.common.fieldDef.QueryParamNames;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author kaisar
 */
public class EadSearchResults {

    private ResponseHeader responseHeader;
    private long numFound;
    private long start;
    private int totalPage;
    private List<EadSearchResult> eadSearchResults;
    private String nextPage;
    private String previousPage;

    public EadSearchResults() {
        eadSearchResults = new ArrayList<EadSearchResult>();
    }

    public EadSearchResults(QueryResponse response, String uri) {
        this();

        SolrDocumentList documentList = response.getResults();
        this.responseHeader = new ResponseHeader(response);
        this.numFound = documentList.getNumFound();
        this.start = documentList.getStart();

        for (SolrDocument document : documentList) {
            this.eadSearchResults.add(new EadSearchResult(document, response));
        }
        this.totalPage = (int) (this.numFound / this.responseHeader.getRows());
        if ((this.numFound % this.responseHeader.getRows() > 0)) {
            this.totalPage++;
        }
        this.setNextPage(uri);
        this.setPreviousPage(uri);
    }

    private void setNextPage(String uri) {
        if (this.start + this.responseHeader.getRows() < this.numFound) {
            this.nextPage = uri + QueryParamNames.QUESTION + QueryParamNames.Q + QueryParamNames.EQUALS + this.responseHeader.getQ()
                    + QueryParamNames.AND + QueryParamNames.START + QueryParamNames.EQUALS + (this.start + this.responseHeader.getRows())
                    + QueryParamNames.AND + QueryParamNames.COUNT + QueryParamNames.EQUALS + this.responseHeader.getRows();
        } else {
            this.nextPage = null;
        }
    }

    private void setPreviousPage(String uri) {
        if (this.start > 0) {
            long preStart = (this.start - this.responseHeader.getRows() < 0) ? 0 : this.start - this.responseHeader.getRows();
            this.previousPage = uri + QueryParamNames.QUESTION + QueryParamNames.Q + QueryParamNames.EQUALS + this.responseHeader.getQ()
                    + QueryParamNames.AND + QueryParamNames.START + QueryParamNames.EQUALS + preStart
                    + QueryParamNames.AND + QueryParamNames.COUNT + QueryParamNames.EQUALS + this.responseHeader.getRows();
        } else {
            this.previousPage = null;
        }
    }

    public String getNextPage() {
        return nextPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public long getNumFound() {
        return numFound;
    }

    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public List<EadSearchResult> getEadSearchResutls() {
        return Collections.unmodifiableList(eadSearchResults);
    }

    public void setEadSearchResutls(List<EadSearchResult> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eadSearchResults = eadSearchResutls;
        }
    }

    public void addEadSearchResul(EadSearchResult eadSearchResutl) {
        this.eadSearchResults.add(eadSearchResutl);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<EadSearchResult> getEadSearchResults() {
        return eadSearchResults;
    }

    public void setEadSearchResults(List<EadSearchResult> eadSearchResults) {
        this.eadSearchResults = eadSearchResults;
    }
}
