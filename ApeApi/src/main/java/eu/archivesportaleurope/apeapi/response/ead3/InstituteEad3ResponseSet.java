/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead3;

import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
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
 * @author kaisar
 */
@XmlRootElement
public class InstituteEad3ResponseSet extends ResponseSet {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<InstituteEad3Response> ead3Results;

    public InstituteEad3ResponseSet() {
        ead3Results = new ArrayList<>();
    }

    public InstituteEad3ResponseSet(QueryResponse response) throws SolrServerException {
        this();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addEadSearchResult(new InstituteEad3Response(document, response));
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<InstituteEad3Response> getEadResults() {
        return Collections.unmodifiableList(ead3Results);
    }

    public final void setEadResults(List<InstituteEad3Response> eadResults) {
        if (eadResults != null) {
            this.ead3Results = eadResults;
        }
    }

    public final void addEadSearchResult(InstituteEad3Response eadResults) {
        if (eadResults != null) {
            this.ead3Results.add(eadResults);
        }
    }
}
