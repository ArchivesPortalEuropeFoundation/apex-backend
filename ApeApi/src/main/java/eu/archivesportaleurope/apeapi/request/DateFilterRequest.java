/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class DateFilterRequest {
    @ApiModelProperty(value="Name of the date field")
    @NotNull(message="Field name can not be null")
    String dateFiledName;
    
    @ApiModelProperty(value="Date field id")
    @NotNull(message="Date field id can not be null")
    String dateFiledId;

    public String getDateFiledName() {
        return dateFiledName;
    }

    public void setDateFiledName(String dateFiledName) {
        this.dateFiledName = dateFiledName;
    }

    public String getDateFiledId() {
        return dateFiledId;
    }

    public void setDateFiledId(String dateFiledId) {
        this.dateFiledId = dateFiledId;
    }
    
    
}
