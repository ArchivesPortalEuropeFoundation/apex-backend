/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.TermsResponse;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class AutocompletionResponseSet {

    private List<AutocompletionResponse> autocompletionResults;

    public AutocompletionResponseSet() {
        autocompletionResults = new ArrayList<>();

    }

    public void add(TermsResponse termsResponse, String type) {
        if (termsResponse == null) {
            return;
        }
        for (Map.Entry<String, List<TermsResponse.Term>> entry : termsResponse.getTermMap().entrySet()) {
            for (TermsResponse.Term termItem : entry.getValue()) {
                if (autocompletionResults.size() < 10) {
                    autocompletionResults.add(new AutocompletionResponse(termItem, type));
                }
            }
        }

    }

    public List<AutocompletionResponse> getAutocompletionResults() {
        return Collections.unmodifiableList(autocompletionResults);
    }

    public void setAutocompletionResults(List<AutocompletionResponse> autocompletionResults) {
        this.autocompletionResults = autocompletionResults;
    }
}
