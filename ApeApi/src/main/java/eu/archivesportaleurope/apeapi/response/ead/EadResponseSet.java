/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class EadResponseSet extends ResponseSet {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<EadResponse> eadSearchResults;

    @ApiModelProperty(value = "Available fields for sort option")
    private Set<String> sortFields;
    
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public EadResponseSet() {
        this.sortFields = new SortFields().getRepresentationFieldName();
        eadSearchResults = new ArrayList<>();
    }

    public EadResponseSet(QueryResponse response) throws SolrServerException {
        this.sortFields = new SortFields().getRepresentationFieldName();
        eadSearchResults = new ArrayList<>();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {
            try {
                EadResponse eadResponse = new EadResponse(document, response);
                this.addEadSearchResult(eadResponse);
            } catch (Exception ex) {
                logger.error("Ead response format error: "+ex.getMessage());
            }
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
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

    public Set<String> getSortFields() {
        return sortFields;
    }

    public void setSortFields(Set<String> sortFields) {
        this.sortFields = sortFields;
    }

}
