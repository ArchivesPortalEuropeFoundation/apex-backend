package eu.apenet.persistence.exception;

/***
 * @author paul
 */

public class RoleTypeException extends PersistenceException{

	private static final long serialVersionUID = 8054696981982350315L;
	public RoleTypeException(String message) {
		super(message);
	}
	public RoleTypeException(Throwable cause){
		super(cause);
	}
	public RoleTypeException(String message, Throwable cause){
		super(message, cause);
	}
}
	
	

