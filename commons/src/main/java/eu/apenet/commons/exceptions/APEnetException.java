package eu.apenet.commons.exceptions;

public class APEnetException extends Exception {

    public APEnetException() {
        super();
    }

    public APEnetException(String message, Throwable cause) {
        super(message, cause);
    }

    public APEnetException(String message) {
        super(message);
    }

    public APEnetException(Throwable cause) {
        super(cause);
    }

}
