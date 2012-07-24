package eu.apenet.persistence.exception;

/***
 * @author paul
 */

public class SearchesException extends PersistenceException{
	
	private static final long serialVersionUID = 2383709176946835469L;
	public SearchesException(String message) {
		super(message);
	}
	public SearchesException(Throwable cause){
		super(cause);
	}
	public SearchesException(String message, Throwable cause){
		super(message, cause);
	}
}
