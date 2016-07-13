/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import de.staatsbibliothek_berlin.eac.EacCpf;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
public class ContentResponseEacCpf {
    @ApiModelProperty(required = true, value="Information in EAD-format, serialized as JSON. This part could potentially contain all the elements that can be part of an EAD/XML-document.")
    private EacCpf content;
    
    public EacCpf getContent() {
        return content;
    }

    public void setContent(EacCpf content) {
        this.content = content;
    }
}
