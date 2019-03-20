/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.hierarchy;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.archivesportaleurope.apeapi.response.ead.*;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
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
        this.unitTitle = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_TITLE));

        this.siblingPosition = CommonUtils.objectToInt(solrDocument.getFieldValue(Ead3SolrFields.ORDER_ID));

        this.unitId = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_ID));
        this.scopeContent = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.SCOPE_CONTENT));
        this.ancestorLevel = parentLevel;
    }

    /**
     * Default constructor
     */
    public HierarchyResponse() {
        //Do nothing
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
