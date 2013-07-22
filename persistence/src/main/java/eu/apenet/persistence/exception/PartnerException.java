package eu.apenet.persistence.exception;

/***
 * @author paul
 */

public class PartnerException extends PersistenceException{

	private static final long serialVersionUID = 6740867512589755475L;
	
	public PartnerException(String message) {
		super(message);
	}
	public PartnerException(Throwable cause){
		super(cause);
	}
	public PartnerException(String message, Throwable cause){
		super(message, cause);
	}
}
	
	

