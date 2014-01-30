package eu.archivesportaleurope.harvester.oaipmh;

import java.io.File;

public class HarvesterParserException extends Exception {

	private File notParsebleResponse;
	private String requestUrl;
	/**
	 * 
	 */
	private static final long serialVersionUID = -382601559328073185L;


	public HarvesterParserException(String requestUrl, File notParsebleResponse, Throwable cause) {
		super(cause);
		this.notParsebleResponse = notParsebleResponse;
		this.requestUrl = requestUrl;
	}

	public File getNotParsebleResponse() {
		return notParsebleResponse;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
