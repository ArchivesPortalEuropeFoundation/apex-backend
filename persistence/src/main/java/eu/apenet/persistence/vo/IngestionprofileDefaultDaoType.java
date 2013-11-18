/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum IngestionprofileDefaultDaoType {
    TEXT(1, "ingestionprofiles.dao.text"),
    IMAGE(2, "ingestionprofiles.dao.image"),
    SOUND(3, "ingestionprofiles.dao.sound"),
    VIDEO(4, "ingestionprofiles.dao.video"),
    THREE_D(5, "ingestionprofiles.dao.3D"),
    UNSPECIFIED(0, "ingestionprofiles.dao.unspecified");

    private int id;
    private String resourceType;

    private IngestionprofileDefaultDaoType(int id, String resourceType){
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

    public String getDaoText() {
        if(isText())
            return "TEXT";
        if(isImage())
            return "IMAGE";
        if(isSound())
            return "SOUND";
        if(isVideo())
            return "VIDEO";
        if(is3D())
            return "3D";
        return "UNSPECIFIED";
    }

    public int getId() {
        return id;
    }

    public String getResourceType(){
        return resourceType;
    }

    public static IngestionprofileDefaultDaoType getDaoType(String id){
        for(IngestionprofileDefaultDaoType daoType : IngestionprofileDefaultDaoType.values()){
            if(daoType.id == Integer.parseInt(id))
                return daoType;
        }
        return null;
    }
}
