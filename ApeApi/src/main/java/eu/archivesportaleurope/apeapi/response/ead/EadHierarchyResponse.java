/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.response.hierarchy.HierarchyResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class EadHierarchyResponse extends InstituteEadResponse {

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
    
    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<HierarchyResponse> ancestors;

    private EadHierarchyResponse(SolrDocument solrDocument, QueryResponse response) {
        super(solrDocument, response);
        this.unitTitle = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_TITLE));
        if (response.getHighlighting().get(id).get(Ead3SolrFields.UNIT_TITLE) != null) {
            this.unitTitleWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(Ead3SolrFields.UNIT_TITLE).get(0));
        }

        this.unitId = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_ID));
        this.scopeContent = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.SCOPE_CONTENT));
        if (response.getHighlighting().get(id).get(Ead3SolrFields.SCOPE_CONTENT) != null) {
            this.scopeContentWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(Ead3SolrFields.SCOPE_CONTENT).get(0));
        }
        Object anc = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_ANCESTORS);
        if (anc != null) {
            this.numberOfAncestors = Integer.parseInt(this.objectToString(anc));
        }
    }

    /**
     * Default constructor
     */
    public EadHierarchyResponse() {
        //Do nothing
    }

    EadHierarchyResponse(SolrDocument document, QueryResponse decendentResponse, Map<String, Integer> ancestorIdList, Map<String, SolrDocument> ancIdDocMap) {
        this(document, decendentResponse);
        this.ancestors = new ArrayList<>();
        ancestorIdList.entrySet().stream().forEach((ancId) -> {
            SolrDocument ancestor = ancIdDocMap.get(ancId.getKey());
            HierarchyResponse ancestorResponse = new HierarchyResponse(ancestor, null, ancId.getValue());
            this.ancestors.add(ancestorResponse);
        });
        this.ancestors.sort((HierarchyResponse a, HierarchyResponse b) -> a.getAncestorLevel()-b.getAncestorLevel());
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

    public List<HierarchyResponse> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<HierarchyResponse> ancestors) {
        this.ancestors = ancestors;
    }
    
    public void addAncestor(HierarchyResponse ancestor) {
        if (null != this.ancestors) {
            this.ancestors.add(ancestor);
        }
    }
}
