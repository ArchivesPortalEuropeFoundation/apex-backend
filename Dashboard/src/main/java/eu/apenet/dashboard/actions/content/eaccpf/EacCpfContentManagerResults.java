/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.eaccpf;

import eu.apenet.dashboard.actions.content.ContentManagerResults;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.EacCpf;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author papp
 */
public class EacCpfContentManagerResults extends ContentManagerResults {

    private List<EacCpfResult> eacCpfResults = new ArrayList<EacCpfResult>();

    public EacCpfContentManagerResults(ContentSearchOptions eadSearchOptions) {
        super(eadSearchOptions);
    }

    public List<EacCpfResult> getEacCpfResults() {
        return eacCpfResults;
    }

    public void setEacCpfResults(List<EacCpfResult> eacCpfResults) {
        this.eacCpfResults = eacCpfResults;
    }

    public void setEacCpfs(List<EacCpf> eacCpfs) {
        for (EacCpf eacCpf : eacCpfs) {
            this.eacCpfResults.add(new EacCpfResult(eacCpf));
        }
    }
}
