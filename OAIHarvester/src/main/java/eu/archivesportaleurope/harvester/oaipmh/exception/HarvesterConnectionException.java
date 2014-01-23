package eu.archivesportaleurope.harvester.oaipmh.exception;

public class HarvesterConnectionException extends HarvesterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1782858939371377316L;

	public HarvesterConnectionException(String requestUrl, Throwable cause) {
		super(requestUrl, cause);
	}

}
