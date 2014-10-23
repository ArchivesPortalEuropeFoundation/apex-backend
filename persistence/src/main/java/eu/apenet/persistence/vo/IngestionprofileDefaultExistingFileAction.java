/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum IngestionprofileDefaultExistingFileAction {

    KEEP(0, "ingestionprofiles.existing.keep"),
    OVERWRITE(1, "ingestionprofiles.existing.overwrite"),
    ASK(2, "ingestionprofiles.existing.ask");

    private int id;
    private String resourceName;

    private IngestionprofileDefaultExistingFileAction(int id, String resourceName) {
        this.id = id;
        this.resourceName = resourceName;
    }

    public boolean isOverwrite() {
        return OVERWRITE.equals(this);
    }

    public boolean isKeep() {
        return KEEP.equals(this);
    }

    public boolean isAsk() {
        return ASK.equals(this);
    }

    public int getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public static IngestionprofileDefaultExistingFileAction getExistingFileAction(String id) {
        for (IngestionprofileDefaultExistingFileAction existingFileAction : IngestionprofileDefaultExistingFileAction.values()) {
            if (existingFileAction.id == Integer.parseInt(id)) {
                return existingFileAction;
            }
        }
        return null;
    }
}
