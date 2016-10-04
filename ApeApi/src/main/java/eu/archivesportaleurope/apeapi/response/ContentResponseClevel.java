/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import gov.loc.ead.C;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
@ApiModel
public class ContentResponseClevel extends ContentResponse {
    @ApiModelProperty(required = true, value="Information of C-level in EAD, serialized as JSON. This part could potentially contain all the elements that can be part of a Component-element (&lt;c&gt;) of an EAD/XML-document. ")
    private C content;

    public ContentResponseClevel() {
    }

    public ContentResponseClevel(DetailContent detailContent, String Id) {
        super(detailContent, Id);
    }
    
    public C getContent() {
        return content;
    }

    public void setContent(C content) {
        this.content = content;
    }
}
