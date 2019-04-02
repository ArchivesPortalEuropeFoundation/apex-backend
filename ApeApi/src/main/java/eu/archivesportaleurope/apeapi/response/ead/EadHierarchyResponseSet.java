/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import eu.archivesportaleurope.apeapi.response.common.SortFields;
import eu.archivesportaleurope.apeapi.response.facet.EadFacetFields;
import eu.archivesportaleurope.apeapi.response.facet.FacetDateFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class EadHierarchyResponseSet extends ResponseSet {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<EadHierarchyResponse> eadSearchResults;

    @ApiModelProperty(value = "Available fields for sort option")
    private Set<String> sortFields;

    @ApiModelProperty(required = true, value = "Array of facet fileds.")
    private EadFacetFields facetFields;

    @ApiModelProperty(required = true, value = "Array of facet date fileds.")
    private FacetDateFields facetDateFields;

    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public EadHierarchyResponseSet() {
        this.sortFields = new SortFields().getRepresentationFieldName();
        eadSearchResults = new ArrayList<>();
        this.facetFields = new EadFacetFields();
        this.facetDateFields = new FacetDateFields();
    }

    public EadHierarchyResponseSet(QueryResponse decendentResponse, Map<String, Map<String, Integer>> descendantAncesMap, Map<String, SolrDocument> ancIdDocMap) {
        this.sortFields = new SortFields().getRepresentationFieldName();
        eadSearchResults = new ArrayList<>();
        this.setFacetFields(new EadFacetFields(decendentResponse));
        this.setFacetDateFields(new FacetDateFields(decendentResponse));
        SearchStatResponse responseHeader = new SearchStatResponse(decendentResponse);

        SolrDocumentList documentList = decendentResponse.getResults();
        if (null != documentList) {
            super.setTotalResults(documentList.getNumFound());
            super.setStartIndex(documentList.getStart());

            for (SolrDocument document : documentList) {
                try {
                    EadHierarchyResponse eadResponse = new EadHierarchyResponse(document, decendentResponse,
                            descendantAncesMap.get(document.getFieldValue(Ead3SolrFields.ID).toString()), ancIdDocMap);
                    this.addEadSearchResult(eadResponse);
                } catch (Exception ex) {
                    logger.error("Ead response format error: " + document.values(), ex);
                }
            }
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<EadHierarchyResponse> getEadSearchResults() {
        return Collections.unmodifiableList(eadSearchResults);
    }

    public final void setEadSearchResults(List<EadHierarchyResponse> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eadSearchResults = eadSearchResutls;
        }
    }

    public final void addEadSearchResult(EadHierarchyResponse eadSearchResutl) {
        if (eadSearchResutl != null) {
            this.eadSearchResults.add(eadSearchResutl);
        }
    }

    public Set<String> getSortFields() {
        return sortFields;
    }

    public void setSortFields(Set<String> sortFields) {
        this.sortFields = sortFields;
    }

    public EadFacetFields getFacetFields() {
        return facetFields;
    }

    public final void setFacetFields(EadFacetFields facetFields) {
        this.facetFields = facetFields;
    }

    public FacetDateFields getFacetDateFields() {
        return facetDateFields;
    }

    public final void setFacetDateFields(FacetDateFields facetDateFields) {
        this.facetDateFields = facetDateFields;
    }

}
