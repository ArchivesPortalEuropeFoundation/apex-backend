/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead3;

import eu.apenet.commons.solr.Ead3SolrFields;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class Ead3Response extends InstituteEad3Response {

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

    private String numberOfDescendents;

    private String numberOfAncestors;

    private List<String> daoType;
    private List<String> daoLinks;

    public Ead3Response(SolrDocument solrDocument, QueryResponse response) {
        super(solrDocument, response);

        this.numberOfDescendents = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DESCENDENTS));
        this.numberOfAncestors = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_ANCESTORS));
        this.unitTitle = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_TITLE));
        if (response.getHighlighting().get(id).get(Ead3SolrFields.UNIT_TITLE) != null) {
            this.unitTitleWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(Ead3SolrFields.UNIT_TITLE).get(0));
        }

        this.unitId = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_ID));
        this.scopeContent = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.SCOPE_CONTENT));
        if (response.getHighlighting().get(id).get(Ead3SolrFields.SCOPE_CONTENT) != null) {
            this.scopeContentWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(Ead3SolrFields.SCOPE_CONTENT).get(0));
        }

        this.daoType = (List) solrDocument.getFieldValue(Ead3SolrFields.DAO_TYPE);
        this.daoLinks = (List) solrDocument.getFieldValue(Ead3SolrFields.DAO_LINKS);
    }

    /**
     * Default constructor
     */
    public Ead3Response() {
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

    public String getNumberOfDescendents() {
        return numberOfDescendents;
    }

    public void setNumberOfDescendents(String numberOfDescendents) {
        this.numberOfDescendents = numberOfDescendents;
    }

    public String getNumberOfAncestors() {
        return numberOfAncestors;
    }

    public void setNumberOfAncestors(String numberOfAncestors) {
        this.numberOfAncestors = numberOfAncestors;
    }

    public List<String> getDaoType() {
        return daoType;
    }

    public void setDaoType(List<String> daoType) {
        this.daoType = daoType;
    }

    public List<String> getDaoLinks() {
        return daoLinks;
    }

    public void setDaoLinks(List<String> daoLinks) {
        this.daoLinks = daoLinks;
    }

}
