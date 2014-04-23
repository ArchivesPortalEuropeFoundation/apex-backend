/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.dashboard.manual.eaccpf.util.MapEntry;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author papp
 */
public class IndexAction extends EacCpfAction {

    private Set<MapEntry> cpfTypeList = new LinkedHashSet<MapEntry>();

    @Override
    public String execute() {
        setUpCpfTypeList();
        setUpLanguages();
        setUpScriptList();
        return SUCCESS;
    }

    private void setUpCpfTypeList() {
        cpfTypeList.add(new MapEntry(PERSON, "a person"));
        cpfTypeList.add(new MapEntry(CORPORATE_BODY, "a corporate body"));
        cpfTypeList.add(new MapEntry(FAMILY, "a family"));
    }

    public Set<MapEntry> getCpfTypeList() {
        return cpfTypeList;
    }

    public void setCpfTypeList(Set<MapEntry> cpfTypeList) {
        this.cpfTypeList = cpfTypeList;
    }
}
