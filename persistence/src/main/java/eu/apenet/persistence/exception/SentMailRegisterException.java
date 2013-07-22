package eu.apenet.persistence.exception;

/***
 * @author paul
 */

public class SentMailRegisterException extends PersistenceException{

	private static final long serialVersionUID = 8054696981982350315L;
	public SentMailRegisterException(String message) {
		super(message);
	}
	public SentMailRegisterException(Throwable cause){
		super(cause);
	}
	public SentMailRegisterException(String message, Throwable cause){
		super(message, cause);
	}
}
	
	

