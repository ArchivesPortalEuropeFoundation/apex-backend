/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.backend.apeapi.entity.ead;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.response.TermsResponse;

/**
 *
 * @author kaisar
 */
public class AutocompletionResults {

    private List<AutocompletionResult> autocompletionResults;

    public AutocompletionResults() {
        autocompletionResults = new ArrayList<>();

    }

    public void add(TermsResponse termsResponse, String type) {
        if (termsResponse != null) {
            for (Map.Entry<String, List<TermsResponse.Term>> entry : termsResponse.getTermMap().entrySet()) {
                for (TermsResponse.Term termItem : entry.getValue()) {
                    if (autocompletionResults.size() < 10) {
                        autocompletionResults.add(new AutocompletionResult(termItem, type));
                    }
                }
            }
        }
    }

    public List<AutocompletionResult> getAutocompletionResults() {
        return Collections.unmodifiableList(autocompletionResults);
    }

    public void setAutocompletionResults(List<AutocompletionResult> autocompletionResults) {
        this.autocompletionResults = autocompletionResults;
    }
}
