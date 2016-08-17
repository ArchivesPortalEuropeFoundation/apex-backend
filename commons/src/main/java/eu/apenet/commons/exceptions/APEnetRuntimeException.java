package eu.apenet.commons.exceptions;

public class APEnetRuntimeException extends RuntimeException {

    public APEnetRuntimeException() {
        super();
    }

    public APEnetRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public APEnetRuntimeException(String message) {
        super(message);
    }

    public APEnetRuntimeException(Throwable cause) {
        super(cause);
    }

}
