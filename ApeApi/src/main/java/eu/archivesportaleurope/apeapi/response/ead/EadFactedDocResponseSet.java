/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.response.facet.EadFacetFields;
import eu.archivesportaleurope.apeapi.response.facet.FacetDateFields;
import io.swagger.annotations.ApiModelProperty;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class EadFactedDocResponseSet extends EadDocResponseSet {

    @ApiModelProperty(required = true, value = "Array of facet fileds.")
    private EadFacetFields facetFields;

    @ApiModelProperty(required = true, value = "Array of facet date fileds.")
    private FacetDateFields facetDateFields;

    public EadFactedDocResponseSet() {
        this.facetFields = new EadFacetFields();
        this.facetDateFields = new FacetDateFields();
    }

    public EadFactedDocResponseSet(SearchDocRequest request, QueryResponse response) throws SolrServerException {
        super(request, response);
        this.setFacetFields(new EadFacetFields(response));
        this.setFacetDateFields(new FacetDateFields(response));
    }

    public EadFacetFields getFacetFields() {
        return this.facetFields;
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
