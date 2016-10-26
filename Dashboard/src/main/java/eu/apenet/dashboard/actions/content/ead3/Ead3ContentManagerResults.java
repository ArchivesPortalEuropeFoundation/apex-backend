/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.ead3;

import eu.apenet.dashboard.actions.content.ContentManagerResults;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.Ead3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author kaisar
 */
public class Ead3ContentManagerResults extends ContentManagerResults {

    private List<Ead3Result> ead3Results = new ArrayList<>();

    public Ead3ContentManagerResults(ContentSearchOptions eadSearchOptions) {
        super(eadSearchOptions);
    }

    public List<Ead3Result> getEad3Results() {
        return Collections.unmodifiableList(ead3Results);
    }

    public void setEad3Results(List<Ead3Result> ead3Results) {
        if (ead3Results != null) {
            this.ead3Results = ead3Results;
        }
    }

    public void setEad3s(List<Ead3> ead3s) {
        for (Ead3 ead3 : ead3s) {
            this.ead3Results.add(new Ead3Result(ead3));
        }
    }

    public void addEad3Results(Ead3Result ead3Result) {
        this.ead3Results.add(ead3Result);
    }

}
