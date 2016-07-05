/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.response.facet.FacetDateFields;
import eu.archivesportaleurope.apeapi.response.facet.EadFacetFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class EadFactedResponseSet extends EadResponseSet {
    @ApiModelProperty(required = true, value="Array of facet fileds.")
    private EadFacetFields facetFields;
    
    @ApiModelProperty(required = true, value="Array of facet date fileds.")
    private FacetDateFields facetDateFields;

    public EadFactedResponseSet() {
        this.facetFields = new EadFacetFields();
        this.facetDateFields = new FacetDateFields();
    }

    public EadFactedResponseSet(QueryResponse response) throws SolrServerException {
        super(response);
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
