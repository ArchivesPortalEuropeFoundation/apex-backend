package eu.apenet.persistence.exception;

public class NuOperationException extends PersistenceException{

	private static final long serialVersionUID = 6740867512589755475L;
	
	public NuOperationException(String message) {
		super(message);
	}
	public NuOperationException(Throwable cause){
		super(cause);
	}
	public NuOperationException(String message, Throwable cause){
		super(message, cause);
	}
}
	
	

