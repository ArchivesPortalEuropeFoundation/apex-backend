/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kaisar
 */
public class SortRequest {

    @ApiModelProperty(value = "Type of sorting. Allowed values can be asc or desc", example = "asc")
    private String sortType = "asc";
    @ApiModelProperty(value = "Sorting fields")
    private List<String> fields = new ArrayList<>();

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
