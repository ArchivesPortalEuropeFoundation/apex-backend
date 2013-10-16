/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum UserprofileDefaultUploadAction {
    CONVERT_VALIDATE_PUBLISH("userprofiles.upload.convertValidatePublish"),
    CONVERT_VALIDATE_PUBLISH_EUROPEANA("userprofiles.upload.convertValidatePublishEuropeana"),
    CONVERT("userprofiles.upload.convert"),
    VALIDATE("userprofiles.upload.validate");
    
    private String resourceName;
    private UserprofileDefaultUploadAction(String resourceName){
        this.resourceName = resourceName;
    }

    public boolean isConvertValidatePublish(){
        return CONVERT_VALIDATE_PUBLISH.equals(this);
    }
    
    public boolean isConvertValidatePublishEuropeana(){
        return CONVERT_VALIDATE_PUBLISH_EUROPEANA.equals(this);
    }
    
    public boolean isConvert(){
        return CONVERT.equals(this);
    }
    
    public boolean isValidate(){
        return VALIDATE.equals(this);
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public static UserprofileDefaultUploadAction getUploadAction(String name) {
        for (UserprofileDefaultUploadAction uploadAction : UserprofileDefaultUploadAction.values()) {
            if (uploadAction.resourceName.equals(name)) {
                return uploadAction;
            }
        }
        return null;
    }
}
