/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Size;

/**
 *
 * @author mahbub
 */
public class ContentRequest {

    @ApiModelProperty(value = "List of ids")
    @Size(min=1, max=1000)
    private List<String> idList = new ArrayList<>();

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public void addField(String field) {
        this.idList.add(field);
    }

}
