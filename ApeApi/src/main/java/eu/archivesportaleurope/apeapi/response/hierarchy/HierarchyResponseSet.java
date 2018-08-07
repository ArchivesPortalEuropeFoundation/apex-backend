/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.hierarchy;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
    private List<HierarchyResponse> results;
    @XmlTransient
    private boolean isChildrenSorted;

    private class HierarchyResponseComparator implements Comparator<HierarchyResponse> {

        @Override
        public int compare(HierarchyResponse res1, HierarchyResponse res2) {
            if (res1.getAncestorLevel() < res2.getAncestorLevel()) {
                return -1;
            } else if (res1.getAncestorLevel() == res2.getAncestorLevel() && res1.getSiblingPosition() < res2.getSiblingPosition()) {
                return -1;
            } else if (res1.getAncestorLevel() > res2.getAncestorLevel()) {
                return 1;
            } else if (res1.getAncestorLevel() == res2.getAncestorLevel() && res1.getSiblingPosition() > res2.getSiblingPosition()) {
                return 1;
            }

            //equal
            return res1.getId().compareToIgnoreCase(res2.getId());
        }
    }

    public HierarchyResponseSet() {
        results = new ArrayList<>();
        isChildrenSorted = false;
    }

    public HierarchyResponseSet(QueryResponse response, Map<String, Integer> keyLevel) throws SolrServerException {
        this();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addResult(new HierarchyResponse(document, response, keyLevel.get(document.getFieldValue(Ead3SolrFields.ID).toString())));
        }

        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public HierarchyResponseSet(QueryResponse response, int childFixedLevel) throws SolrServerException {
        this();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addResult(new HierarchyResponse(document, response, childFixedLevel));
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<HierarchyResponse> getResults() {
        if (!this.isChildrenSorted && this.results != null) {
            this.results.sort(new HierarchyResponseComparator());
        }
        return Collections.unmodifiableList(results);
    }

    public final void setResults(List<HierarchyResponse> results) {
        if (results != null) {
            this.isChildrenSorted = false;
            this.results = results;
        }
    }

    public final void addResult(HierarchyResponse children) {
        this.isChildrenSorted = false;
        this.results.add(children);
    }
}
