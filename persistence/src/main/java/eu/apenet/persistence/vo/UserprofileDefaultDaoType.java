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
    TEXT("userprofiles.dao.text"),
    IMAGE("userprofiles.dao.image"),
    SOUND("userprofiles.dao.sound"),
    VIDEO("userprofiles.dao.video"),
    THREE_D("userprofiles.dao.3D"),
    UNSPECIFIED("userprofiles.dao.unspecified");
    
    private String resourceType;
    private UserprofileDefaultDaoType(String resourceType){
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
