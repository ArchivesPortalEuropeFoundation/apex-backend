package eu.archivesportaleurope.harvester.oaipmh.exception;


public class HarvesterException extends Exception {

	private String requestUrl;
	/**
	 * 
	 */
	private static final long serialVersionUID = -382601559328073185L;


	public HarvesterException(String requestUrl, Throwable cause) {
		super(cause);
		this.requestUrl = requestUrl;
	}

	public HarvesterException(String requestUrl, String message) {
		super(message);
		this.requestUrl = requestUrl;
	}
	public String getRequestUrl() {
		return requestUrl;
	}



	
}
