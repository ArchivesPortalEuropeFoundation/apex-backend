package eu.apenet.persistence.hibernate;

public class BadConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1853014458099667097L;

	public BadConfigurationException() {
		super();
	}

	public BadConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadConfigurationException(String message) {
		super(message);
	}

	public BadConfigurationException(Throwable cause) {
		super(cause);
	}

}
