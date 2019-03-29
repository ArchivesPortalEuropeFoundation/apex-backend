/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class SearchFilterRequest {

    @ApiModelProperty(value = "Name of the facet field")
    @NotNull(message = "Field name can not be null")
    String facetFieldName;

    @ApiModelProperty(value = "List of field ids")
    @NotNull(message = "Field value can not be null")
    List<String> facetFieldIds;

    public String getFacetFieldName() {
        return facetFieldName;
    }

    public void setFacetFieldName(String facetFieldName) {
        this.facetFieldName = facetFieldName;
    }

    public List<String> getFacetFieldIds() {
        return facetFieldIds;
    }

    public void setFacetFieldIds(List<String> facetFieldIds) {
        this.facetFieldIds = facetFieldIds;
    }
}
