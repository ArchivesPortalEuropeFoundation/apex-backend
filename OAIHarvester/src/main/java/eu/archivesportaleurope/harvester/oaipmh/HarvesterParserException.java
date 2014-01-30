package eu.archivesportaleurope.harvester.oaipmh;

import java.io.File;

public class HarvesterParserException extends Exception {

	private File notParsebleResponse;
	/**
	 * 
	 */
	private static final long serialVersionUID = -382601559328073185L;

	public HarvesterParserException(File notParsebleResponse, Throwable cause) {
		super(cause);
		this.notParsebleResponse = notParsebleResponse;
	}

	public File getNotParsebleResponse() {
		return notParsebleResponse;
	}

	
}
