/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.response.ResponseSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(required = true, value = "Total number of EAD documents found.")
    private int totalDocs;
    @ApiModelProperty(required = true, value="Array of search result, total number of elements can be less than query limit.")
    private List<EadDocResponse> eadDocList;

    public EadDocResponseSet() {
        eadDocList = new ArrayList<>();
    }

    public EadDocResponseSet(SearchDocRequest request, QueryResponse response) {
        this();
        GroupCommand command = response.getGroupResponse().getValues().get(0);
        this.setTotalDocs(command.getNGroups());
        super.setTotalResults(command.getMatches());
        super.setStartIndex(request.getStartIndex());
        super.setTotalPages((int) (this.totalDocs / request.getCount()));
        if (this.totalDocs % request.getCount() > 0) {
            super.totalPages++;
        }

        for (Group group : response.getGroupResponse().getValues().get(0).getValues()) {
            if (group.getGroupValue() != null) {
                this.addEadDoc(new EadDocResponse(group));
            }
        }
    }

    public int getTotalDocs() {
        return totalDocs;
    }

    public final void setTotalDocs(int totalDocs) {
        this.totalDocs = totalDocs;
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
