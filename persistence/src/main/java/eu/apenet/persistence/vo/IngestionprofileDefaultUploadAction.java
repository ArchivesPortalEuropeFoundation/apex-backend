/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

/**
 *
 * @author papp
 */
public enum IngestionprofileDefaultUploadAction {

    NOTHING(0, "ingestionprofiles.upload.nothing"),
    CONVERT_VALIDATE_PUBLISH(1, "ingestionprofiles.upload.convertValidatePublish"),
    CONVERT_VALIDATE_PUBLISH_EUROPEANA(2, "ingestionprofiles.upload.convertValidatePublishEuropeana"),
    CONVERT(3, "ingestionprofiles.upload.convert"),
    VALIDATE(4, "ingestionprofiles.upload.validate");

    private String resourceName;
    private int id;

    private IngestionprofileDefaultUploadAction(int id, String resourceName) {
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

    public static IngestionprofileDefaultUploadAction getUploadAction(String id) {
        for (IngestionprofileDefaultUploadAction uploadAction : IngestionprofileDefaultUploadAction.values()) {
            if (uploadAction.id == Integer.parseInt(id)) {
                return uploadAction;
            }
        }
        return null;
    }
}
