/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.hierarchy;

import eu.archivesportaleurope.apeapi.response.ead.*;
import eu.apenet.commons.solr.SolrFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class HierarchyResponse extends InstituteEadResponse {

    @ApiModelProperty(value = "Identifier of the result provided by the repository")
    private String unitId;

    @ApiModelProperty(value = "Description of the result")
    private String unitTitle;

    @ApiModelProperty(value = "More descriptive information about the result. ")
    private String scopeContent;
    
    @ApiModelProperty(value = "Index relative to its siblings")
    private int siblingPosition;
    
    @ApiModelProperty(value = "Greatest ancestor is a level 0")
    private int ancestorLevel;

    public HierarchyResponse(SolrDocument solrDocument, QueryResponse response, int parentLevel) {
        super(solrDocument, response);
        this.unitTitle = this.objectToString(solrDocument.getFieldValue(SolrFields.TITLE));
        
        this.siblingPosition = Integer.parseInt(this.objectToString(solrDocument.getFieldValue(SolrFields.ORDER_ID)));

        this.unitId = this.objectToString(solrDocument.getFieldValue(SolrFields.UNITID));
        this.scopeContent = this.objectToString(solrDocument.getFieldValue(SolrFields.SCOPECONTENT));
        this.ancestorLevel = parentLevel;
    }

    /**
     * Default constructor
     */
    public HierarchyResponse() {
        //Do nothing
    }

    private String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getScopeContent() {
        return scopeContent;
    }

    public void setScopeContent(String scopeContent) {
        this.scopeContent = scopeContent;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public int getSiblingPosition() {
        return siblingPosition;
    }

    public void setSiblingPosition(int siblingPosition) {
        this.siblingPosition = siblingPosition;
    }

    public int getAncestorLevel() {
        return ancestorLevel;
    }

    public void setAncestorLevel(int ancestorLevel) {
        this.ancestorLevel = ancestorLevel;
    }
}
