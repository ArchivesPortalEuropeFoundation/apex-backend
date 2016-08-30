/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 * @author kaisar
 */
public class SortFilterRequest {

    @ApiModelProperty(value = "Type of sorting. Allowed values can be asc or desc")
    private String sortType = "asc";
    @ApiModelProperty(value = "Sorting fields")
    private List<String> fields;

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void addField(String field) {
        this.fields.add(field);
    }

}
