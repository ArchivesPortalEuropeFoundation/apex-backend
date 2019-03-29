/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class SearchDocRequest extends QueryPageRequest {

    @ApiModelProperty(value = "Document type", example = "fa")
    @NotNull(message = "Document type can not be null")
    String docType;

    @ApiModelProperty(value = "List of search filters using facet filed")
    List<SearchFilterRequest> filters;

    @ApiModelProperty(value = "List of search filters using date")
    List<DateFilterRequest> dateFilters;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public List<SearchFilterRequest> getFilters() {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        return filters;
    }

    public void setFilters(List<SearchFilterRequest> filters) {
        this.filters = filters;
    }

    public List<DateFilterRequest> getDateFilters() {
        if (dateFilters == null) {
            dateFilters = new ArrayList<>();
        }
        return dateFilters;
    }

    public void setDateFilters(List<DateFilterRequest> dateFilters) {
        this.dateFilters = dateFilters;
    }
}
