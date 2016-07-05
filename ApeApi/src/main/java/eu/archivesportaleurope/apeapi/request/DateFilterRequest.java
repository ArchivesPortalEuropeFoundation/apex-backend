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
    String dateFieldName;
    
    @ApiModelProperty(value="Date field id")
    @NotNull(message="Date field id can not be null")
    String dateFieldId;

    public String getDateFieldName() {
        return dateFieldName;
    }

    public void setDateFieldName(String dateFieldName) {
        this.dateFieldName = dateFieldName;
    }

    public String getDateFieldId() {
        return dateFieldId;
    }

    public void setDateFieldId(String dateFieldId) {
        this.dateFieldId = dateFieldId;
    }
    
    
}
