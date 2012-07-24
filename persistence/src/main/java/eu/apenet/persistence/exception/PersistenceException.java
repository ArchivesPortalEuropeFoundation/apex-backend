package eu.apenet.persistence.exception;

/**
 * Specific Expection for persistence module
 * @author paul
 *
 */

public class PersistenceException extends RuntimeException {

	private static final long serialVersionUID = 4062414065303550901L;

	public PersistenceException() {
		super();
	}
	
	public PersistenceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(Exception original) {
		super("Exception come from"+original);
	}
	
	public PersistenceException(Throwable cause) {
		super("Exception caused by"+cause);
	}

}

