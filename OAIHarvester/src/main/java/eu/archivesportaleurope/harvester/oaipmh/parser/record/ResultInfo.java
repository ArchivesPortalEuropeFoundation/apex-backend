package eu.archivesportaleurope.harvester.oaipmh.parser.record;

import java.util.ArrayList;
import java.util.List;

public class ResultInfo {

	private List<String> errors = new ArrayList<String>();
	private String newResumptionToken;
	private String identifier;
	private String requestUrl;
	public List<String> getErrors() {
		return errors;
	}
	public String getNewResumptionToken() {
		return newResumptionToken;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public void setNewResumptionToken(String newResumptionToken) {
		this.newResumptionToken = newResumptionToken;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
}
