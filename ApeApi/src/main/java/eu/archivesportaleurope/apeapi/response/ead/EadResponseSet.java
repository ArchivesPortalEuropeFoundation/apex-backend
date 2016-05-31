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
import org.apache.solr.client.solrj.response.FacetField;
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
    
    private FacetFields facetFields = new FacetFields();

    public EadResponseSet() {
        eadSearchResults = new ArrayList<>();
    }

    public EadResponseSet(QueryResponse response) throws SolrServerException {
        this();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        this.totalResults = documentList.getNumFound();
        this.startIndex = documentList.getStart();

        for (SolrDocument document : documentList) {
            this.eadSearchResults.add(new EadResponse(document, response));
        }
        this.totalPages = (int) (this.totalResults / responseHeader.getRows());
        if (this.totalResults % responseHeader.getRows() > 0) {
            this.totalPages++;
        }
        facetFields.setCountry(response.getFacetField("country"));
//        List<FacetField> facets = response.getFacetFields();
//        for (FacetField ff : facets) {
//            facetFields.setCountries(ff.);
//        }
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public List<EadResponse> getEadSearchResults() {
        return Collections.unmodifiableList(eadSearchResults);
    }

    public void setEadSearchResults(List<EadResponse> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eadSearchResults = eadSearchResutls;
        }
    }

    public void addEadSearchResult(EadResponse eadSearchResutl) {
        this.eadSearchResults.add(eadSearchResutl);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public FacetFields getFacetFields() {
        return facetFields;
    }

    public void setFacetFields(FacetFields facetFields) {
        this.facetFields = facetFields;
    }
    
}
