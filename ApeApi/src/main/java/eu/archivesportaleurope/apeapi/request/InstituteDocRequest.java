/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
@ApiModel
public class InstituteDocRequest {

    @ApiModelProperty(value="Institute's ID")
    @NotNull(message="Institute's ID can not be null")
    String instituteId;
    
    @ApiModelProperty(value="Document type")
    @NotNull(message="Document type can not be null")
    String docType;
    
    @ApiModelProperty(value="Number of items to retrieve. Default is 5, maximum is 50.")
    @Max(value = 50, message = "Count must not be more than 50")
    int count;
    
    @ApiModelProperty(value="Offset the list of returned results by this amount. Default is zero.")
    int startIndex;

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
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

}
