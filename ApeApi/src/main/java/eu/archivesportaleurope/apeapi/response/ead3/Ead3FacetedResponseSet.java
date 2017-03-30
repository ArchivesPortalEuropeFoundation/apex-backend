/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead3;

import eu.archivesportaleurope.apeapi.response.facet.Ead3FacetDateFields;
import eu.archivesportaleurope.apeapi.response.facet.Ead3FacetFields;
import io.swagger.annotations.ApiModelProperty;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class Ead3FacetedResponseSet extends Ead3ResponseSet {

    @ApiModelProperty(required = true, value = "Array of facet fileds.")
    private Ead3FacetFields facetFields;

    @ApiModelProperty(required = true, value = "Array of facet date fileds.")
    private Ead3FacetDateFields facetDateFields;

    public Ead3FacetedResponseSet() {
        this.facetFields = new Ead3FacetFields();
        this.facetDateFields = new Ead3FacetDateFields();
    }

    public Ead3FacetedResponseSet(QueryResponse response) throws SolrServerException {
        super(response);
        this.setFacetFields(new Ead3FacetFields(response));
        this.setFacetDateFields(new Ead3FacetDateFields(response));
    }

    public Ead3FacetFields getFacetFields() {
        return this.facetFields;
    }

    public final void setFacetFields(Ead3FacetFields facetFields) {
        this.facetFields = facetFields;
    }

    public Ead3FacetDateFields getFacetDateFields() {
        return facetDateFields;
    }

    public final void setFacetDateFields(Ead3FacetDateFields facetDateFields) {
        this.facetDateFields = facetDateFields;
    }
}
