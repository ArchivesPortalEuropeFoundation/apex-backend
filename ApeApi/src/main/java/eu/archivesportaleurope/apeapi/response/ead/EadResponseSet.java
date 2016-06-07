/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import eu.archivesportaleurope.apeapi.response.facet.FacetFields;
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
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class EadResponseSet {
    @ApiModelProperty(required = true, value="Total number of documents found.")
    private long totalResults;
    @ApiModelProperty(required = true, value="Position from where this response set started.")
    private long startIndex;
    @ApiModelProperty(required = true, value="Total pages that can be generated based on query limit.")
    private int totalPages;
    @ApiModelProperty(required = true, value="Array of search result, total number of elements can be less than query limit.")
    private List<EadResponse> eadSearchResults;

    public EadResponseSet() {
        eadSearchResults = new ArrayList<>();
    }

    public EadResponseSet(QueryResponse response) throws SolrServerException {
        eadSearchResults = new ArrayList<>();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        this.setTotalResults(documentList.getNumFound());
        this.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {
            this.addEadSearchResult(new EadResponse(document, response));
        }
        this.setTotalPages((int)(this.totalResults / responseHeader.getRows()));
        if (this.totalResults % responseHeader.getRows() > 0) {
            this.totalPages++;
        }
    }

    public long getTotalResults() {
        return totalResults;
    }

    public final void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public final void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public List<EadResponse> getEadSearchResults() {
        return Collections.unmodifiableList(eadSearchResults);
    }

    public final void setEadSearchResults(List<EadResponse> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eadSearchResults = eadSearchResutls;
        }
    }

    public final void addEadSearchResult(EadResponse eadSearchResutl) {
        if (eadSearchResutl != null) {
            this.eadSearchResults.add(eadSearchResutl);
        }
    }

    public int getTotalPages() {
        return totalPages;
    }

    public final void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
