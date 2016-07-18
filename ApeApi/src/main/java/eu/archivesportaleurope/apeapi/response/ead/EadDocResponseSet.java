/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.response.ResponseSet;
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
public class EadDocResponseSet extends ResponseSet {

    private String searchTerm;
    private int totalDocs;
    private List<EadDocResponse> eadDocList;

    public EadDocResponseSet() {
        eadDocList = new ArrayList<>();
    }

    public EadDocResponseSet(SearchDocRequest request, List<FacetField.Count> eads, int totalRes, EadDocResponse.Type type) {
        this();
        this.totalDocs = totalRes;
        this.setSearchTerm(request.getQuery());
        super.setTotalResults(request.getCount()); ///todo:change
        super.setStartIndex(request.getStartIndex());

        if (eads != null) {
            for (FacetField.Count ead : eads) {
                EadDocResponse value = new EadDocResponse(ead, type);
                this.eadDocList.add(value);
            }
        }
    }

    public int getTotalDocs() {
        return totalDocs;
    }

    public void setTotalDocs(int totalDocs) {
        this.totalDocs = totalDocs;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }

    public final void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public List<EadDocResponse> getEadDocList() {
        return eadDocList;
    }

    public void setEadDocList(List<EadDocResponse> eadDocList) {
        this.eadDocList = eadDocList;
    }

}
