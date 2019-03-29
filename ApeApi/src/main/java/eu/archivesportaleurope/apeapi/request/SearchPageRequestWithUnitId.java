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
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class SearchPageRequestWithUnitId extends PageRequest {

    @ApiModelProperty(value = "Finding Aid No")
    @NotNull(message = "Finding Aid No can not be null")
    String findingAidNo;

    @ApiModelProperty(value = "Unit Id of the level", example = " ")
    String unitId;

    @ApiModelProperty(value = "List of search filters using facet filed")
    List<SearchFilterRequest> filters;

    @ApiModelProperty(value = "List of search filters using date")
    List<DateFilterRequest> dateFilters;

    @ApiModelProperty(value = "Sorting result with fields and sorting type")
    SortRequest sortRequest;

    public String getFindingAidNo() {
        return findingAidNo;
    }

    public void setFindingAidNo(String findingAidNo) {
        this.findingAidNo = findingAidNo;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public SortRequest getSortRequest() {
        return sortRequest;
    }

    public void setSortRequest(SortRequest sortRequest) {
        this.sortRequest = sortRequest;
    }

}
