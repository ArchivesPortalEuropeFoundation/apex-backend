/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.backend.apeapi.entity.ead;

import org.apache.solr.client.solrj.response.TermsResponse;

/**
 *
 * @author kaisar
 */
public class AutocompletionResult implements Comparable<AutocompletionResult> {

    private String type;
    private String term;
    private long frequency;

    protected AutocompletionResult(TermsResponse.Term term, String type) {
        this.type = type;
        this.term = term.getTerm();
        this.frequency = term.getFrequency();
    }

    public String getType() {
        return type;
    }

    public String getTerm() {
        return term;
    }

    public long getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(AutocompletionResult o) {
        if (frequency > o.getFrequency()) {
            return -1;
        } else if (frequency < o.getFrequency()) {
            return 1;
        }
        return 0;
    }

}
