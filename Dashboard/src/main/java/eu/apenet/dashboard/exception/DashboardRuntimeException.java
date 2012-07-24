package eu.apenet.dashboard.exception;

public class DashboardRuntimeException extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DashboardRuntimeException() {
		super();
	}
	
	public DashboardRuntimeException (String mensaje) {
		super(mensaje);
	}
	
	public DashboardRuntimeException (Throwable cause){
		super(cause);
	}
	
	public DashboardRuntimeException(String message, Throwable cause){
		super(message, cause);
	}


}