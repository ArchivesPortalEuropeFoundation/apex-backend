/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.InstituteEadResponse;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class EadDocMock extends InstituteEadResponse {

    private String dynamicName;
    private String dynamicId;
    @ApiModelProperty(required = true, value = "Identifier of the result provided by the repository")
    private String unitId;

    @ApiModelProperty(required = true, value = "Description of the result")
    private String unitTitle;

    @ApiModelProperty(required = true, value = "Description of the result, with the mark <b>&lt;em&gt;</b> to emphasize the search term that was used in the search request.")
    private String unitTitleWithHighlighting;

    @ApiModelProperty(required = true, value = "More descriptive information about the result. ")
    private String scopeContent;

    @ApiModelProperty(required = true, value = "More descriptive information about the result, with the mark <b>&lt;em&gt;</b> to emphasize the search term that was used in the search request.")
    private String scopeContentWithHighlighting;

    public EadDocMock(SolrDocument solrDocument, QueryResponse response) {
        super(solrDocument, response);
        this.dynamicName = this.objectToString(solrDocument.getFieldValue(SolrFields.FA_DYNAMIC_NAME));
        this.dynamicId = this.objectToString(solrDocument.getFieldValue(SolrFields.FA_DYNAMIC_ID + "0_s"));
    }

    public EadDocMock() {
    }

    public String getDynamicName() {
        return dynamicName;
    }

    public void setDynamicName(String dynamicName) {
        this.dynamicName = dynamicName;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getUnitTitleWithHighlighting() {
        return unitTitleWithHighlighting;
    }

    public void setUnitTitleWithHighlighting(String unitTitleWithHighlighting) {
        this.unitTitleWithHighlighting = unitTitleWithHighlighting;
    }

    public String getScopeContent() {
        return scopeContent;
    }

    public void setScopeContent(String scopeContent) {
        this.scopeContent = scopeContent;
    }

    public String getScopeContentWithHighlighting() {
        return scopeContentWithHighlighting;
    }

    public void setScopeContentWithHighlighting(String scopeContentWithHighlighting) {
        this.scopeContentWithHighlighting = scopeContentWithHighlighting;
    }

    private String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }
}
