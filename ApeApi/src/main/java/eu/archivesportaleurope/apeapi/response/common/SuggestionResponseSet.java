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

    private final long numFound;
    private final long numOfSuggestionFound;
    private final String type;
    private final List<SuggestionResponse> suggestionResults;

    public SuggestionResponseSet(QueryResponse queryResponse) throws SolrServerException {
        this.numFound = queryResponse.getResults().getNumFound();
        if (this.numFound > 0) {
            this.type = "others";
        } else {
            this.type = "misspelled";
        }

        suggestionResults = new ArrayList<>();
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

    public String getType() {
        return type;
    }

    public List<SuggestionResponse> getSuggestionResults() {
        return Collections.unmodifiableList(suggestionResults);
    }

    public long getNumOfSuggestionFound() {
        return numOfSuggestionFound;
    }

}
