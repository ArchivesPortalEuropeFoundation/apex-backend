/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import gov.loc.ead.C;

/**
 *
 * @author mahbub
 */
public class ContentResponseClevel extends ContentResponse {
    private C content;
    
    public C getContent() {
        return content;
    }

    public void setContent(C content) {
        this.content = content;
    }
}
