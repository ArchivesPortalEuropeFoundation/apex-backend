/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.backend.apeapi.entity.ead;

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

    public EadSearchResults() {
        eadSearchResults = new ArrayList<EadSearchResult>();
    }

    public EadSearchResults(QueryResponse response) {
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
