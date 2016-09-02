/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.eaccpf;

import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import eu.archivesportaleurope.apeapi.response.common.SortFields;
import io.swagger.annotations.ApiModel;
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
@ApiModel
public class EacCpfResponseSet extends ResponseSet {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<EacCpfResponse> eacSearchResults;

    @ApiModelProperty(value = "Available fields for sort option")
    private Set<String> sortFields;

    public EacCpfResponseSet() {
        this.sortFields = new SortFields().getRepresentationEacFieldName();
        eacSearchResults = new ArrayList<>();
    }

    public EacCpfResponseSet(QueryResponse response) throws SolrServerException {
        this.sortFields = new SortFields().getRepresentationEacFieldName();
        eacSearchResults = new ArrayList<>();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addEadSearchResult(new EacCpfResponse(document, response));
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<EacCpfResponse> getEacSearchResults() {
        return Collections.unmodifiableList(eacSearchResults);
    }

    public final void setEacSearchResults(List<EacCpfResponse> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eacSearchResults = eadSearchResutls;
        }
    }

    public final void addEadSearchResult(EacCpfResponse eadSearchResutl) {
        if (eadSearchResutl != null) {
            this.eacSearchResults.add(eadSearchResutl);
        }
    }

    public Set<String> getSortFields() {
        return sortFields;
    }

    public void setSortFields(Set<String> sortFields) {
        this.sortFields = sortFields;
    }

}
