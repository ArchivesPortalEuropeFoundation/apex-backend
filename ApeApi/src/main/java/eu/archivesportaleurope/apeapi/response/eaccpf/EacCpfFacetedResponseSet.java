/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.eaccpf;

import eu.archivesportaleurope.apeapi.response.facet.EacFacetFields;
import eu.archivesportaleurope.apeapi.response.facet.FacetDateFields;
import io.swagger.annotations.ApiModelProperty;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class EacCpfFacetedResponseSet extends EacCpfResponseSet {

    @ApiModelProperty(required = true, value = "Array of facet fileds.")
    private EacFacetFields facetFields;

    @ApiModelProperty(required = true, value = "Array of facet date fileds.")
    private FacetDateFields facetDateFields;

    public EacCpfFacetedResponseSet(EacFacetFields facetFields, FacetDateFields facetDateFields) {
        this.facetFields = new EacFacetFields();
        this.facetDateFields = new FacetDateFields();
    }

    public EacCpfFacetedResponseSet(QueryResponse response) throws SolrServerException {
        super(response);
        this.setFacetDateFields(new FacetDateFields(response));
        this.setFacetFields(new EacFacetFields(response));
    }

    public EacFacetFields getFacetFields() {
        return facetFields;
    }

    public final void setFacetFields(EacFacetFields facetFields) {
        this.facetFields = facetFields;
    }

    public FacetDateFields getFacetDateFields() {
        return facetDateFields;
    }

    public final void setFacetDateFields(FacetDateFields facetDateFields) {
        this.facetDateFields = facetDateFields;
    }

}
