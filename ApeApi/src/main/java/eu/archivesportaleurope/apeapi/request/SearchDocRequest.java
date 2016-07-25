/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class SearchDocRequest extends PageRequest {

    @ApiModelProperty(value = "Search query string", example="*")
    @NotNull(message = "Search query can not be null")
    String query;

    @ApiModelProperty(value = "Document type")
    @NotNull(message = "Document type can not be null")
    String docType;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
