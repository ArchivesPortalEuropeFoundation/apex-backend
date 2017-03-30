/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.apenet.commons.solr.SolrFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class EadResponse extends InstituteEadResponse {

    @ApiModelProperty(value = "Identifier of the result provided by the repository")
    private String unitId;

    @ApiModelProperty(value = "Description of the result")
    private String unitTitle;

    @ApiModelProperty(value = "Description of the result, with the mark <b>&lt;em&gt;</b> to emphasize the search term that was used in the search request.")
    private String unitTitleWithHighlighting;

    @ApiModelProperty(value = "More descriptive information about the result. ")
    private String scopeContent;

    @ApiModelProperty(value = "More descriptive information about the result, with the mark <b>&lt;em&gt;</b> to emphasize the search term that was used in the search request.")
    private String scopeContentWithHighlighting;

    @ApiModelProperty(value = "Number of Ancestors")
    private int numberOfAncestors = 0;

    public EadResponse(SolrDocument solrDocument, QueryResponse response) {
        super(solrDocument, response);
        this.unitTitle = this.objectToString(solrDocument.getFieldValue(SolrFields.TITLE));
        if (response.getHighlighting().get(id).get(SolrFields.TITLE) != null) {
            this.unitTitleWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(SolrFields.TITLE).get(0));
        }

        this.unitId = this.objectToString(solrDocument.getFieldValue(SolrFields.UNITID));
        this.scopeContent = this.objectToString(solrDocument.getFieldValue(SolrFields.SCOPECONTENT));
        if (response.getHighlighting().get(id).get(SolrFields.SCOPECONTENT) != null) {
            this.scopeContentWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(SolrFields.SCOPECONTENT).get(0));
        }
        Object anc = solrDocument.getFieldValue(SolrFields.NO_OF_ANCESTORS);
        if (anc != null) {
            this.numberOfAncestors = Integer.parseInt(this.objectToString(anc));
        }
    }

    /**
     * Default constructor
     */
    public EadResponse() {
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

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public int getNumberOfAncestors() {
        return numberOfAncestors;
    }

    public void setNumberOfAncestors(int numberOfAncestors) {
        this.numberOfAncestors = numberOfAncestors;
    }

}
