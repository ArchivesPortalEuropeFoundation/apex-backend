/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.response.facet.FacetFields;
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
    private FacetFields facetFields;

    public EadFactedResponseSet() {
        this.facetFields = new FacetFields();
    }

    public EadFactedResponseSet(QueryResponse response) throws SolrServerException {
        super(response);
        this.setFacetFields(new FacetFields(response));
    }
    
    
    public FacetFields getFacetFields() {
        return this.facetFields;
    }

    public final void setFacetFields(FacetFields facetFields) {
        this.facetFields = facetFields;
    }
}
