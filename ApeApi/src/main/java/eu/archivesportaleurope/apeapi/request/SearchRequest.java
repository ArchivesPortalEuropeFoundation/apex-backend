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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class SearchRequest extends QueryPageRequest {

    @ApiModelProperty(value = "List of search filters using facet filed")
    List<SearchFilterRequest> filters;

    @ApiModelProperty(value = "List of search filters using date")
    List<DateFilterRequest> dateFilters;

    @ApiModelProperty(value = "Sorting result with fields and sorting type")
    SortRequest sortRequest;

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

    public SortRequest getSortRequest() {
        return sortRequest;
    }

    public void setSortRequest(SortRequest sortRequest) {
        this.sortRequest = sortRequest;
    }
}
