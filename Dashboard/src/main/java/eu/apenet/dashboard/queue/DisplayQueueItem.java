package eu.apenet.dashboard.queue;

public class DisplayQueueItem {

	private int id;
	private String action;
	private Integer priority;
	private String errors;
	private String archivalInstitution;
	private String eadidOrFilename;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	public String getArchivalInstitution() {
		return archivalInstitution;
	}
	public void setArchivalInstitution(String archivalInstitution) {
		this.archivalInstitution = archivalInstitution;
	}
	public String getEadidOrFilename() {
		return eadidOrFilename;
	}
	public void setEadidOrFilename(String eadidOrFilename) {
		this.eadidOrFilename = eadidOrFilename;
	}
	
}
