package eu.archivesportaleurope.harvester.oaipmh.exception;

public class HarvesterInterruptionException extends HarvesterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1782858939371377316L;

	public HarvesterInterruptionException(String requestUrl, Throwable cause) {
		super(requestUrl, cause);
	}

	public HarvesterInterruptionException(String requestUrl, String message) {
		super(requestUrl, message);
	}

}
