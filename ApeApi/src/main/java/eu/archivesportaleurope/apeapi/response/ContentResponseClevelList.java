/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class ContentResponseClevelList {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<ContentResponseClevel> results;
    

    public ContentResponseClevelList() {
        results = new ArrayList<>();
    }

    public List<ContentResponseClevel> getResults() {
        
        return Collections.unmodifiableList(results);
    }

    public final void setResults(List<ContentResponseClevel> results) {
        if (results != null) {
            this.results = results;
        }
    }

    public final void addResult(ContentResponseClevel children) {
        this.results.add(children);
    }
}
