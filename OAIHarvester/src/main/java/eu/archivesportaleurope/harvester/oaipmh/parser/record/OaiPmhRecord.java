package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.util.Date;

public class OaiPmhRecord {
	private String identifier;
	private String status;
	private String filename;
    private Date timestamp;
    private boolean dropped;
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
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
	public boolean isDropped() {
		return dropped;
	}
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
    
}
