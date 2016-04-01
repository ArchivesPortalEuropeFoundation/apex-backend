/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import eu.archivesportaleurope.apeapi.response.common.SuggestionResponseSet;
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
    //private SuggestionResponseSet suggestions;

    public EadResponseSet() {
        eadSearchResults = new ArrayList<>();
    }

    public EadResponseSet(QueryResponse response) throws SolrServerException {
        this();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        this.totalResults = documentList.getNumFound();
        this.start = documentList.getStart();

        for (SolrDocument document : documentList) {
            this.eadSearchResults.add(new EadResponse(document, response));
        }
        this.totalPages = (int) (this.totalResults / responseHeader.getRows());
        if (this.totalResults % responseHeader.getRows() > 0) {
            this.totalPages++;
        }
//        this.suggestions = new SuggestionResponseSet(response);
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

//    public SuggestionResponseSet getSuggestions() {
//        return suggestions;
//    }
//
//    public void setSuggestions(SuggestionResponseSet suggestions) {
//        this.suggestions = suggestions;
//    }
}
