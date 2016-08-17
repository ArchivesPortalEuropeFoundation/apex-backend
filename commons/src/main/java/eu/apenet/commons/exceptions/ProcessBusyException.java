package eu.apenet.commons.exceptions;

public class ProcessBusyException extends Exception {

    public ProcessBusyException() {
        super("Process is busy");
    }

    public ProcessBusyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessBusyException(String message) {
        super(message);
    }

    public ProcessBusyException(Throwable cause) {
        super(cause);
    }

}
