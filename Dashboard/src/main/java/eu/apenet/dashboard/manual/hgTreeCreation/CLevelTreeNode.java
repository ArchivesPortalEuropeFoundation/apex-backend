package eu.apenet.dashboard.manual.hgTreeCreation;

import eu.apenet.persistence.vo.CLevel;

import java.io.File;

/**
 * User: Yoann Moranville
 * Date: 20/09/2011
 *
 * @author Yoann Moranville
 */

public class CLevelTreeNode extends CLevel {
    private String description;
    private File file;

    public CLevelTreeNode(String unittitle){
        super();
        setUnittitle(unittitle);
        this.description = "";
    }

    public CLevelTreeNode(String unitid, String unittitle){
        super();
        setUnittitle(unittitle);
        setUnitid(unitid);
        this.description = "";
    }

    public String getDescription() {
        return description;
    }

    public CLevelTreeNode setDescription(String description) {
        this.description = description;
        return this;
    }

    public void setFile(File file){
        this.file = file;
    }

    public boolean isFile(){
        return file != null;
    }

    public File getFile(){
        if(isFile())
            return file;
        return null;
    }

    public boolean equals(Object obj){
        return obj instanceof eu.apenet.dashboard.manual.hgTreeCreation.CLevelTreeNode && this == obj;
    }

    public String toString(){
        String idString = "";
        if(getUnitid() != null && !getUnitid().equals(""))
            idString += "[" + getUnitid() + "] ";

        if(description != null && !description.equals("")){
            if(description.length() > 15)
                return idString + getUnittitle() + " - " + description.substring(0, 14) + "...";
            return idString + getUnittitle() + " - " + description;
        }
        return idString + getUnittitle();
    }
}
