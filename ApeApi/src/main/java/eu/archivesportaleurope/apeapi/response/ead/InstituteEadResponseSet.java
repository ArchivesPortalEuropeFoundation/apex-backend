/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class InstituteEadResponseSet extends ResponseSet {
    @ApiModelProperty(required = true, value="Array of search result, total number of elements can be less than query limit.")
    private List<InstituteEadResponse> eadResults;

    public InstituteEadResponseSet() {
        eadResults = new ArrayList<>();
    }

    public InstituteEadResponseSet(QueryResponse response) throws SolrServerException {
        eadResults = new ArrayList<>();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {
            
            this.addEadSearchResult(new InstituteEadResponse(document, response));
        }
        this.setTotalPages((int)(super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    
    public List<InstituteEadResponse> getEadResults() {
        return Collections.unmodifiableList(eadResults);
    }

    public final void setEadResults(List<InstituteEadResponse> eadResults) {
        if (eadResults != null) {
            this.eadResults = eadResults;
        }
    }

    public final void addEadSearchResult(InstituteEadResponse eadResults) {
        if (eadResults != null) {
            this.eadResults.add(eadResults);
        }
    }

    
}
