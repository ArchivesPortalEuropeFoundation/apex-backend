package org.oclc.oai.harvester.parser.record;

import java.util.ArrayList;
import java.util.List;

public class ResultInfo {

	private List<OaiPmhRecord> records = new ArrayList<OaiPmhRecord>();
	private List<String> errors = new ArrayList<String>();
	private String newResumptionToken;
	private String identifier;
	public List<OaiPmhRecord> getRecords() {
		return records;
	}
	public List<String> getErrors() {
		return errors;
	}
	public String getNewResumptionToken() {
		return newResumptionToken;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setRecords(List<OaiPmhRecord> records) {
		this.records = records;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public void setNewResumptionToken(String newResumptionToken) {
		this.newResumptionToken = newResumptionToken;
	}
	
	
}
