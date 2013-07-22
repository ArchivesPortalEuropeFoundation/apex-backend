package eu.apenet.persistence.exception;

public class POperationException extends PersistenceException{

	private static final long serialVersionUID = -159153274811682210L;
	public POperationException(String message) {
		super(message);
	}
	public POperationException(Throwable cause){
		super(cause);
	}
	public POperationException(String message, Throwable cause){
		super(message, cause);
	}
}
	