package eu.apenet.persistence.vo;

public enum OaiPmhStatus {
	FAILED("label.harvesting.failed"), SUCCEED("label.harvesting.succeed"),SUCCEED_WITH_WARNINGS("label.harvesting.succeedwithwarnings")
	,SUCCEED_WITH_ERRORS("label.harvesting.succeedwitherrors"),PROCESSING("label.harvesting.processing"),FIRST_TIME("label.harvesting.firsttime");

	private String resourceName;
	private OaiPmhStatus(String resourceName){
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

}
