package by.beg.payment_system.exception;

public class UnremovableStatusException extends RuntimeException {

    public UnremovableStatusException() {
        super();
    }

    public UnremovableStatusException(String message) {
        super(message);
    }

    public UnremovableStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnremovableStatusException(Throwable cause) {
        super(cause);
    }
}
