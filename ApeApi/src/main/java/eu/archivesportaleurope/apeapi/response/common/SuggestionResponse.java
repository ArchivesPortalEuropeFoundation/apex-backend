/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.SpellCheckResponse;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class SuggestionResponse {

    private String term;
    private long frequency;
    
    /**
     * Default constructor
     */
    public SuggestionResponse() {
        //Do nothing
    }
    
    public SuggestionResponse(SpellCheckResponse.Collation collation) {
        this.term = collation.getCollationQueryString();
        this.frequency = collation.getNumberOfHits();
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

}
