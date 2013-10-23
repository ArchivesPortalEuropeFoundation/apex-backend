/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum UserprofileDefaultNoEadidAction {
    REMOVE(0, "userprofiles.noeadid.remove"),
    ADD_LATER(1, "userprofiles.noeadid.addLater");

    private int id;
    private String resourceName;

    private UserprofileDefaultNoEadidAction(int id, String resourceName) {
        this.id = id;
        this.resourceName = resourceName;
    }

    public boolean isRemove(){
        return REMOVE.equals(this);
    }

    public boolean isAddLater(){
        return ADD_LATER.equals(this);
    }

    public int getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public static UserprofileDefaultNoEadidAction getExistingFileAction(String name) {
        for (UserprofileDefaultNoEadidAction noEadidAction : UserprofileDefaultNoEadidAction.values()) {
            if (noEadidAction.resourceName.equals(name)) {
                return noEadidAction;
            }
        }
        return null;
    }
}
