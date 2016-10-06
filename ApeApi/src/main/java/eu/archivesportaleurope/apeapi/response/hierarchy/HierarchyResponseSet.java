/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.hierarchy;

import eu.archivesportaleurope.apeapi.response.ead.*;
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
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class HierarchyResponseSet extends ResponseSet {

    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
    private List<HierarchyResponse> children;

    public HierarchyResponseSet() {
        children = new ArrayList<>();
    }

    public HierarchyResponseSet(QueryResponse response) throws SolrServerException {
        this();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addEadSearchResult(new HierarchyResponse(document, response));
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<HierarchyResponse> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public final void setChildren(List<HierarchyResponse> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.children = eadSearchResutls;
        }
    }

    public final void addEadSearchResult(HierarchyResponse eadSearchResutl) {
        if (eadSearchResutl != null) {
            this.children.add(eadSearchResutl);
        }
    }
}
