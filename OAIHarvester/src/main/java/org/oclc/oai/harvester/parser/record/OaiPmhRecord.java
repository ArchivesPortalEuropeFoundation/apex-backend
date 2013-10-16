package org.oclc.oai.harvester.parser.record;

public class OaiPmhRecord {
	private String identifier;
	private String status;
	private String filename;
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public boolean isDeleted(){
		return "deleted".equalsIgnoreCase(status);
	}
	
}
