/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class PageRequest {
    @ApiModelProperty(value="Number of items to retrieve. Default is 5, maximum is 50.", example="5")
    @Max(value = 50, message = "Count must not be more than 50")
    @Min(value = 0, message = "Count must not be less than 0")
    int count;
    
    @ApiModelProperty(value="Offset the list of returned results by this amount. Default is zero.")
    int startIndex;
    
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
