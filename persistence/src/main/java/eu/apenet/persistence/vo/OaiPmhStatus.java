package eu.apenet.persistence.vo;

public enum OaiPmhStatus {
	FAILED("label.harvesting.failed", "FAILED"), SUCCEED("label.harvesting.succeed", "SUCCEED"),SUCCEED_WITH_WARNINGS("label.harvesting.succeedwithwarnings","SUCCEED, WITH WARNINGS")
	,SUCCEED_WITH_ERRORS("label.harvesting.succeedwitherrors", "SUCCEED, WITH ERRORS"),PROCESSING("label.harvesting.processing", "PROCESSING");

	private String resourceName;
	private String name;
	private OaiPmhStatus(String resourceName, String name){
		this.resourceName = resourceName;
		this.name = name;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getName() {
		return name;
	}

	
}
