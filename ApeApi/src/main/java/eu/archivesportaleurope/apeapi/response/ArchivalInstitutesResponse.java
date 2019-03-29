/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.apenet.persistence.vo.ArchivalInstitution;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kaisar
 */
public class ArchivalInstitutesResponse {

    @ApiModelProperty(required = true, value = "Total number of AI found.")
    private long totalResults;
    @ApiModelProperty(required = true, value = "Array of search result, total number of elements can be less than query limit.")
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

    public long getTotalResults() {
        return totalResults;
    }

    public final void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
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
