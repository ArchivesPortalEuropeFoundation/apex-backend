/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class SuggestionResponseSet {

    private long numFound;
    private long numOfSuggestionFound;
    private String type;
    private List<SuggestionResponse> suggestionResults;

    public SuggestionResponseSet() {
        suggestionResults = new ArrayList<>();
    }

    public SuggestionResponseSet(QueryResponse queryResponse) throws SolrServerException {
        this();
        this.numFound = queryResponse.getResults().getNumFound();
        if (this.numFound > 0) {
            this.type = "others";
        } else {
            this.type = "misspelled";
        }
        
        List<SpellCheckResponse.Collation> collations = queryResponse.getSpellCheckResponse().getCollatedResults();
        if (collations != null) {
            for (SpellCheckResponse.Collation collation : queryResponse.getSpellCheckResponse().getCollatedResults()) {
                this.suggestionResults.add(new SuggestionResponse(collation));
            }
        }
        this.numOfSuggestionFound = this.suggestionResults.size();
    }

    public long getNumFound() {
        return numFound;
    }

    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }

    public long getNumOfSuggestionFound() {
        return numOfSuggestionFound;
    }

    public void setNumOfSuggestionFound(long numOfSuggestionFound) {
        this.numOfSuggestionFound = numOfSuggestionFound;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public List<SuggestionResponse> getSuggestionResults() {
        return Collections.unmodifiableList(suggestionResults);
    }

    public void setSuggestionResults(List<SuggestionResponse> suggestionResults) {
        if (suggestionResults != null) {
            this.suggestionResults = suggestionResults;
        }
    }

}
