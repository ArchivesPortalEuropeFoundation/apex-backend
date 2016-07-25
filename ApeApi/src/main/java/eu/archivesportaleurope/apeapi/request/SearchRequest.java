/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class SearchRequest extends PageRequest {

    @ApiModelProperty(value="Search query string", example="*")
    @NotNull(message="Search query can not be null")
    String query;
    
    @ApiModelProperty(value="List of search filters using facet filed")
    ArrayList<SearchFilterRequest> filters;
    
    @ApiModelProperty(value="List of search filters using date")
    ArrayList<DateFilterRequest> dateFilters;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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
