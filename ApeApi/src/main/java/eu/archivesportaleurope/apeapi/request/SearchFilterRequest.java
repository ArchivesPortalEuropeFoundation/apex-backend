/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class SearchFilterRequest {
    @ApiModelProperty(value="Name of the facet field")
    @NotNull(message="Field name can not be null")
    String facetFiledName;
    
    @ApiModelProperty(value="List of field ids")
    @NotNull(message="Field value can not be null")
    ArrayList<String> facetFieldIds;

    public String getFacetFiledName() {
        return facetFiledName;
    }

    public void setFacetFiledName(String facetFiledName) {
        this.facetFiledName = facetFiledName;
    }

    public ArrayList<String> getFacetFieldIds() {
        return facetFieldIds;
    }

    public void setFacetFieldIds(ArrayList<String> facetFieldIds) {
        this.facetFieldIds = facetFieldIds;
    }
}
