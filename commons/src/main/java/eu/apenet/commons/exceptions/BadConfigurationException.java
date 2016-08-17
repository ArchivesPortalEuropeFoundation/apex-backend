package eu.apenet.commons.exceptions;

public class BadConfigurationException extends APEnetRuntimeException {

    private static final long serialVersionUID = -4415778617083473261L;

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
