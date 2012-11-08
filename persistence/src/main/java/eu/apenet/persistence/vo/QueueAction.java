package eu.apenet.persistence.vo;

public enum QueueAction {
	VALIDATE("content.message.validate"), CONVERT("content.message.convert"), PUBLISH("content.message.publish"), CONVERT_VALIDATE_PUBLISH("content.message.doitall"), UNPUBLISH("content.message.unpublish"), DELETE("content.message.delete"), CONVERT_TO_ESE_EDM("content.message.convert.ese"),DELETE_ESE_EDM("content.message.delete.ese"), DELIVER_TO_EUROPEANA("content.message.deliver.europeana"), DELETE_FROM_EUROPEANA("content.message.delete.europeana" );

	private String resourceName;
	private QueueAction(String resourceName){
		this.resourceName = resourceName;
	}
	

	public boolean isValidateAction() {
		return VALIDATE.equals(this) || CONVERT_VALIDATE_PUBLISH.equals(this);
	}

	public boolean isConvertAction() {
		return CONVERT.equals(this) || CONVERT_VALIDATE_PUBLISH.equals(this);
	}

	public boolean isPublishAction() {
		return PUBLISH.equals(this) || CONVERT_VALIDATE_PUBLISH.equals(this);
	}

	public boolean isConvertToEseEdmAction() {
		return CONVERT_TO_ESE_EDM.equals(this);
	}
	public boolean isDeleteEseEdmAction() {
		return DELETE_ESE_EDM.equals(this);
	}
	public boolean isDeliverToEuropeanaAction() {
		return DELIVER_TO_EUROPEANA.equals(this);
	}
	public boolean isUnpublishAction() {
		return UNPUBLISH.equals(this);
	}
	public boolean isDeleteAction() {
		return DELETE.equals(this);
	}
	public boolean isDeleteFromEuropeanaAction() {
		return DELETE_FROM_EUROPEANA.equals(this);
	}


	public String getResourceName() {
		return resourceName;
	}
	
}
