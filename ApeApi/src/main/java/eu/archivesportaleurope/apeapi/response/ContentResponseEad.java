/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import gov.loc.ead.Ead;

/**
 *
 * @author mahbub
 */
public class ContentResponseEad extends ContentResponse {
    private Ead content;
    
    public Ead getContent() {
        return content;
    }

    public void setContent(Ead content) {
        this.content = content;
    }
}
