/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum UserprofileDefaultDaoType {
    TEXT(1, "userprofiles.dao.text"),
    IMAGE(2, "userprofiles.dao.image"),
    SOUND(3, "userprofiles.dao.sound"),
    VIDEO(4, "userprofiles.dao.video"),
    THREE_D(5, "userprofiles.dao.3D"),
    UNSPECIFIED(0, "userprofiles.dao.unspecified");

    private int id;
    private String resourceType;

    private UserprofileDefaultDaoType(int id, String resourceType){
        this.id = id;
        this.resourceType = resourceType;
    }

    public boolean isText(){
        return TEXT.equals(this);
    }

    public boolean isImage(){
        return IMAGE.equals(this);
    }

    public boolean isSound(){
        return SOUND.equals(this);
    }

    public boolean isVideo(){
        return VIDEO.equals(this);
    }

    public boolean is3D(){
        return THREE_D.equals(this);
    }

    public boolean isUnspecified(){
        return UNSPECIFIED.equals(this);
    }

    public int getId() {
        return id;
    }

    public String getResourceType(){
        return resourceType;
    }

    public static UserprofileDefaultDaoType getDaoType(String name){
        for(UserprofileDefaultDaoType daoType : UserprofileDefaultDaoType.values()){
            if(daoType.resourceType.equals(name))
                return daoType;
        }
        return null;
    }
}
