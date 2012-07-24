package eu.apenet.persistence.exception;

/***
 * @author paul
 */

public class TemporalUserRegisterException extends PersistenceException{

	private static final long serialVersionUID = 2332737670662711078L;
	public TemporalUserRegisterException(String message) {
		super(message);
	}
	public TemporalUserRegisterException(Throwable cause){
		super(cause);
	}
	public TemporalUserRegisterException(String message, Throwable cause){
		super(message, cause);
	}
}
	
	

