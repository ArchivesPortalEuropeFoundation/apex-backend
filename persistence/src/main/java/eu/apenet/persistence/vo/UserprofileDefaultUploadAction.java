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

    NOTHING(0, "userprofiles.upload.nothing"),
    CONVERT_VALIDATE_PUBLISH(1, "userprofiles.upload.convertValidatePublish"),
    CONVERT_VALIDATE_PUBLISH_EUROPEANA(2, "userprofiles.upload.convertValidatePublishEuropeana"),
    CONVERT(3, "userprofiles.upload.convert"),
    VALIDATE(4, "userprofiles.upload.validate");

    private String resourceName;
    private int id;

    private UserprofileDefaultUploadAction(int id, String resourceName) {
        this.id = id;
        this.resourceName = resourceName;
    }

    public boolean isConvertValidatePublish() {
        return CONVERT_VALIDATE_PUBLISH.equals(this);
    }

    public boolean isConvertValidatePublishEuropeana() {
        return CONVERT_VALIDATE_PUBLISH_EUROPEANA.equals(this);
    }

    public boolean isConvert() {
        return CONVERT.equals(this);
    }

    public boolean isValidate() {
        return VALIDATE.equals(this);
    }

    public String getResourceName() {
        return resourceName;
    }

    public int getId() {
        return id;
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
