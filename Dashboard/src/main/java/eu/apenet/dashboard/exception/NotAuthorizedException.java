package eu.apenet.dashboard.exception;

import eu.apenet.commons.exceptions.APEnetRuntimeException;

public class NotAuthorizedException extends APEnetRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2247894055813620768L;
	public NotAuthorizedException() {
		super();
	}
	public NotAuthorizedException(String message) {
		super(message);
	}


}
