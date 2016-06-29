/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.eaccpf;

import eu.archivesportaleurope.apeapi.response.facet.FacetFields;
import io.swagger.annotations.ApiModelProperty;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class EacCpfFacetedResponseSet extends EacCpfResponseSet {

    @ApiModelProperty(required = true, value = "Array of facet fileds.")
    private FacetFields facetFields;

    public EacCpfFacetedResponseSet(QueryResponse response) throws SolrServerException {
        super(response);
        this.setFacetFields(new FacetFields(response));
    }

    public FacetFields getFacetFields() {
        return facetFields;
    }

    public final void setFacetFields(FacetFields facetFields) {
        this.facetFields = facetFields;
    }
}
