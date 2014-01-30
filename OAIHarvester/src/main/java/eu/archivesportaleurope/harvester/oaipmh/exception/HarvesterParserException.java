package eu.archivesportaleurope.harvester.oaipmh.exception;

import java.io.File;

public class HarvesterParserException extends HarvesterException {

	private File notParsebleResponse;
	/**
	 * 
	 */
	private static final long serialVersionUID = -382601559328073185L;


	public HarvesterParserException(String requestUrl, File notParsebleResponse, Throwable cause) {
		super(requestUrl,cause);
		this.notParsebleResponse = notParsebleResponse;
	}

	public File getNotParsebleResponse() {
		return notParsebleResponse;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
