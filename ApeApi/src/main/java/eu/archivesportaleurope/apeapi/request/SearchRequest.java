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
public class SearchRequest {

    @ApiModelProperty(value="Search query string")
    @NotNull(message="Search query can not be null")
    String query;
    
    @ApiModelProperty(value="Number of items to retrieve. Default is 5, maximum is 50.")
    @Max(value = 50, message = "Count must not be more than 50")
    int count;
    
    @ApiModelProperty(value="Offset the list of returned results by this amount. Default is zero.")
    int startIndex;
    
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
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
