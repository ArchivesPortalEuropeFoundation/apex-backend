package eu.apenet.persistence.exception;

/***
 * @author paul
 */

public class NormalUserException extends PersistenceException{
	
	private static final long serialVersionUID = 6740867512589755475L;
	public NormalUserException(String message) {
		super(message);
	}
	public NormalUserException(Throwable cause){
		super(cause);
	}
	public NormalUserException(String message, Throwable cause){
		super(message, cause);
	}
}
	
	

