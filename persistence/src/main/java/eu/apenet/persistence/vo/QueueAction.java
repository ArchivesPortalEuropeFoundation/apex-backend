package eu.apenet.persistence.vo;

public enum QueueAction {
    VALIDATE("content.message.validate"), CONVERT("content.message.convert"),
    PUBLISH("content.message.publish"), REPUBLISH("content.message.republish"),
    CONVERT_VALIDATE("content.message.convertandvalidate"), CONVERT_VALIDATE_PUBLISH("content.message.doitall"),
    UNPUBLISH("content.message.unpublish"), DELETE("content.message.delete"),
    CONVERT_TO_ESE_EDM("content.message.convert.ese"), DELETE_ESE_EDM("content.message.delete.ese"),
    DELIVER_TO_EUROPEANA("content.message.deliver.europeana"), DELETE_FROM_EUROPEANA("content.message.delete.europeana"),
    UNPUBLISH_ALL("content.message.unpublish.all"), OVERWRITE("content.message.overwrite"),
    CHANGE_TO_DYNAMIC("content.message.dynamic.change"), CHANGE_TO_STATIC("content.message.static.change"),
    USE_PROFILE("content.message.useprofile"), ENABLE_OPEN_DATA("content.message.enableOpenData"), VALIDATE_PUBLISH("content.message.validatepublish");

    private String resourceName;

    private QueueAction(String resourceName) {
        this.resourceName = resourceName;
    }

    public boolean isValidateAction() {
        return VALIDATE.equals(this) || CONVERT_VALIDATE_PUBLISH.equals(this) || CONVERT_VALIDATE.equals(this);
    }

    public boolean isConvertAction() {
        return CONVERT.equals(this) || CONVERT_VALIDATE_PUBLISH.equals(this) || CONVERT_VALIDATE.equals(this);
    }

    public boolean isPublishAction() {
        return PUBLISH.equals(this) || CONVERT_VALIDATE_PUBLISH.equals(this);
    }

    public boolean isRePublishAction() {
        return REPUBLISH.equals(this);
    }

    public boolean isConvertToEseEdmAction() {
        return CONVERT_TO_ESE_EDM.equals(this);
    }

    public boolean isDeleteEseEdmAction() {
        return DELETE_ESE_EDM.equals(this) || UNPUBLISH_ALL.equals(this);
    }

    public boolean isDeliverToEuropeanaAction() {
        return DELIVER_TO_EUROPEANA.equals(this);
    }

    public boolean isUnpublishAction() {
        return UNPUBLISH.equals(this) || UNPUBLISH_ALL.equals(this);
    }

    public boolean isDeleteAction() {
        return DELETE.equals(this);
    }

    public boolean isDeleteFromEuropeanaAction() {
        return DELETE_FROM_EUROPEANA.equals(this) || UNPUBLISH_ALL.equals(this);
    }

    public boolean isOverwriteAction() {
        return OVERWRITE.equals(this);
    }

    public boolean isCreateDynamicEadAction() {
        return CHANGE_TO_DYNAMIC.equals(this);
    }

    public boolean isCreateStaticEadAction() {
        return CHANGE_TO_STATIC.equals(this);
    }

    public boolean isUseProfileAction() {
        return USE_PROFILE.equals(this);
    }

    public String getResourceName() {
        return resourceName;
    }

}
