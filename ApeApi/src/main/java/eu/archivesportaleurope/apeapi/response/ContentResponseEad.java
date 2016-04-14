/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import gov.loc.ead.Ead;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
public class ContentResponseEad extends ContentResponse {
    @ApiModelProperty(required = true, value="Information in EAD-format, serialized as JSON. This part could potentially contain all the elements that can be part of an EAD/XML-document.")
    private Ead content;
    
    public Ead getContent() {
        return content;
    }

    public void setContent(Ead content) {
        this.content = content;
    }
}
