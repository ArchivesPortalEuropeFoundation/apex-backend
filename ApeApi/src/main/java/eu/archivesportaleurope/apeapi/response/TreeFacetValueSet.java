/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.FacetField;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class TreeFacetValueSet {

    private String searchTerm;
    private long totalNumberOfResults;
    private long start;
    private List<TreeFacetValue> treeFacetValues;

    public TreeFacetValueSet() {
        treeFacetValues = new ArrayList<>();
        this.totalNumberOfResults = 0;
    }

    public TreeFacetValueSet(SearchRequest request, List<FacetField.Count> eads, TreeFacetValue.Type type) {
        this();
        this.searchTerm = request.getQuery();
        this.totalNumberOfResults = request.getCount();
        this.start = request.getStartIndex();
        if (eads != null) {
            for (FacetField.Count ead : eads) {
                TreeFacetValue value = new TreeFacetValue(ead, type);
                this.treeFacetValues.add(value);
            }
        }
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public long getTotalNumberOfResults() {
        return totalNumberOfResults;
    }

    public void setTotalNumberOfResults(long totalNumberOfResults) {
        this.totalNumberOfResults = totalNumberOfResults;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public List<TreeFacetValue> getTreeFacetValues() {
        return treeFacetValues;
    }

    public void setTreeFacetValues(List<TreeFacetValue> treeFacetValues) {
        this.treeFacetValues = treeFacetValues;
    }

}
