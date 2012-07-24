package eu.apenet.dashboard.exception;

public class DashboardAPEnetException extends Exception {
	
    private static final long serialVersionUID = -8579602666806773470L;

	public DashboardAPEnetException() {
		super();
	}
	
	public DashboardAPEnetException (String mensaje) {
		super(mensaje);
	}
	
	public DashboardAPEnetException (Throwable cause){
		super(cause);
	}
	
	public DashboardAPEnetException(String message, Throwable cause){
		super(message, cause);
	}


}