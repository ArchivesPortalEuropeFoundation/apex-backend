/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.TermsResponse;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class AutocompletionResponse implements Comparable<AutocompletionResponse> {

    private String type;
    private String term;
    private long frequency;

    public AutocompletionResponse() {
    }

    protected AutocompletionResponse(TermsResponse.Term term, String type) {
        this();
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

    public void setType(String type) {
        this.type = type;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(AutocompletionResponse o) {
        if (frequency > o.getFrequency()) {
            return -1;
        } else if (frequency < o.getFrequency()) {
            return 1;
        }
        return 0;
    }

}