package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class OaiPmhRecord {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private String identifier;
	private String status;
	private String filename;
    private Date timestamp;
    private boolean dropped;
	public String getIdentifier() {
		return identifier;
	}
	public String getFilenameFromIdentifier(){
		if (StringUtils.isNotBlank(identifier)){
			return identifier.replaceAll("[^a-zA-Z0-9\\-\\.]", "_") +".xml";
		}
		return null;
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
	@Override
	public String toString() {
		String result = "'" + identifier;
		result += "' ("+ DATE_FORMAT.format(timestamp) + ")";
		
		return result;
	}
    
}
