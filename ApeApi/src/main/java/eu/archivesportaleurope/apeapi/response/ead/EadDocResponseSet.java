/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ResponseSet;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;

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

    public EadDocResponseSet(SearchRequest request, QueryResponse response, EadDocResponse.Type type) {
        this();
        GroupCommand command = response.getGroupResponse().getValues().get(0);
        this.setTotalDocs(command.getNGroups());
        this.setSearchTerm(request.getQuery());
        super.setTotalResults(command.getMatches());
        super.setStartIndex(request.getStartIndex());

        for (Group group : response.getGroupResponse().getValues().get(0).getValues()) {
            this.addEadDoc(new EadDocResponse(group, type));
        }
    }

    public int getTotalDocs() {
        return totalDocs;
    }

    public final void setTotalDocs(int totalDocs) {
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

    public final void addEadDoc(EadDocResponse docResponse) {
        eadDocList.add(docResponse);
    }

    public void setEadDocList(List<EadDocResponse> eadDocList) {
        this.eadDocList = eadDocList;
    }

}
