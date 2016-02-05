/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.opendata.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class EadResponseSet {

    private long totalResults;
    private long start;
    private int totalPages;
    private List<EadResponse> eadSearchResults;

    public EadResponseSet() {
        eadSearchResults = new ArrayList<>();
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public List<EadResponse> getEadSearchResults() {
        return Collections.unmodifiableList(eadSearchResults);
    }

    public void setEadSearchResults(List<EadResponse> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eadSearchResults = eadSearchResutls;
        }
    }

    public void addEadSearchResult(EadResponse eadSearchResutl) {
        this.eadSearchResults.add(eadSearchResutl);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
