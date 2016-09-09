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
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class SearchDocRequest extends QueryPageRequest {
    @ApiModelProperty(value = "Document type")
    @NotNull(message = "Document type can not be null")
    String docType;

    @ApiModelProperty(value = "List of search filters using facet filed")
    ArrayList<SearchFilterRequest> filters;

    @ApiModelProperty(value = "List of search filters using date")
    ArrayList<DateFilterRequest> dateFilters;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public ArrayList<SearchFilterRequest> getFilters() {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        return filters;
    }

    public void setFilters(ArrayList<SearchFilterRequest> filters) {
        this.filters = filters;
    }

    public ArrayList<DateFilterRequest> getDateFilters() {
        if (dateFilters == null) {
            dateFilters = new ArrayList<>();
        }
        return dateFilters;
    }

    public void setDateFilters(ArrayList<DateFilterRequest> dateFilters) {
        this.dateFilters = dateFilters;
    }
}
