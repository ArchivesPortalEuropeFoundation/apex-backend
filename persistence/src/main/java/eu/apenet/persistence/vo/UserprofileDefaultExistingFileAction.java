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

    OVERWRITE("userprofiles.existing.overwrite"),
    KEEP("userprofiles.existing.keep");
    private String resourceName;

    private UserprofileDefaultExistingFileAction(String resourceName) {
        this.resourceName = resourceName;
    }

    public boolean isOverwrite() {
        return OVERWRITE.equals(this);
    }

    public boolean isKeep() {
        return KEEP.equals(this);
    }

    public String getResourceName() {
        return resourceName;
    }

    public static UserprofileDefaultExistingFileAction getExistingFileAction(String name) {
        for (UserprofileDefaultExistingFileAction existingFileAction : UserprofileDefaultExistingFileAction.values()) {
            if (existingFileAction.resourceName.equals(name)) {
                return existingFileAction;
            }
        }
        return null;
    }
}
