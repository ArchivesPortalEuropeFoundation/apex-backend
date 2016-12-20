/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead3;

import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import eu.archivesportaleurope.apeapi.response.common.SortFields;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
public class Ead3ResponseSet extends ResponseSet {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<Ead3Response> ead3SearchResults;

    @ApiModelProperty(value = "Available fields for sort option")
    private Set<String> sortFields;

    public Ead3ResponseSet() {
        this.sortFields = new SortFields().getRepresentationFieldName();
        ead3SearchResults = new ArrayList<>();
    }

    public Ead3ResponseSet(QueryResponse response) throws SolrServerException {
        this.sortFields = new SortFields().getRepresentationFieldName();
        ead3SearchResults = new ArrayList<>();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addEadSearchResult(new Ead3Response(document, response));
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<Ead3Response> getEadSearchResults() {
        return Collections.unmodifiableList(ead3SearchResults);
    }

    public final void setEadSearchResults(List<Ead3Response> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.ead3SearchResults = eadSearchResutls;
        }
    }

    public final void addEadSearchResult(Ead3Response eadSearchResutl) {
        if (eadSearchResutl != null) {
            this.ead3SearchResults.add(eadSearchResutl);
        }
    }

    public Set<String> getSortFields() {
        return sortFields;
    }

    public void setSortFields(Set<String> sortFields) {
        this.sortFields = sortFields;
    }
}
