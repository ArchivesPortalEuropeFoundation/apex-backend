/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.apenet.persistence.vo.ArchivalInstitution;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class ArchivalInstitutesResponse {

    private long total;
    private List<ArchivalInstituteResponse> institutes;

    public ArchivalInstitutesResponse() {
        this.institutes = new ArrayList<>();
    }

    public ArchivalInstitutesResponse(List<ArchivalInstitution> ais) {
        this();
        for (ArchivalInstitution ai : ais) {
            this.addInstitutes(new ArchivalInstituteResponse(ai));
        }
    }

    public ArchivalInstitutesResponse(QueryResponse response) {
        this();
        this.setTotal(response.getGroupResponse().getValues().get(0).getNGroups());
        for (Group group : response.getGroupResponse().getValues().get(0).getValues()) {
            this.addInstitutes(new ArchivalInstituteResponse(group));
        }
    }

    public long getTotal() {
        return total;
    }

    public final void setTotal(long total) {
        this.total = total;
    }

    public List<ArchivalInstituteResponse> getInstitutes() {
        return institutes;
    }

    public void setInstitutes(List<ArchivalInstituteResponse> institutes) {
        this.institutes = institutes;
    }

    public final void addInstitutes(ArchivalInstituteResponse ai) {
        this.institutes.add(ai);
    }

}
