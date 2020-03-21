package by.beg.payment_system.exception.user_exception;

public class NoAccessException extends Exception {

    public NoAccessException() {
        super();
    }

    public NoAccessException(String message) {
        super(message);
    }

    public NoAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAccessException(Throwable cause) {
        super(cause);
    }
}
