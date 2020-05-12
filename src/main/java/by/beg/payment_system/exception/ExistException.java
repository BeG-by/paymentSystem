package by.beg.payment_system.exception;

public class ExistException extends RuntimeException {

    public ExistException() {
    }

    public ExistException(String message) {
        super(message);
    }

    public ExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistException(Throwable cause) {
        super(cause);
    }
}
