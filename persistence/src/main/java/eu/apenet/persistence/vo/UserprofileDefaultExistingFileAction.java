/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum UserprofileDefaultExistingFileAction {

    KEEP(0, "userprofiles.existing.keep"),
    OVERWRITE(1, "userprofiles.existing.overwrite");

    private int id;
    private String resourceName;

    private UserprofileDefaultExistingFileAction(int id, String resourceName) {
        this.id = id;
        this.resourceName = resourceName;
    }

    public boolean isOverwrite() {
        return OVERWRITE.equals(this);
    }

    public boolean isKeep() {
        return KEEP.equals(this);
    }

    public int getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public static UserprofileDefaultExistingFileAction getExistingFileAction(String id) {
        for (UserprofileDefaultExistingFileAction existingFileAction : UserprofileDefaultExistingFileAction.values()) {
            if (existingFileAction.id == Integer.parseInt(id)) {
                return existingFileAction;
            }
        }
        return null;
    }
}
