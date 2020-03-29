package by.beg.payment_system.exception;

public class UserBlockedException extends Exception {

    public UserBlockedException() {
    }

    public UserBlockedException(String message) {
        super(message);
    }

    public UserBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserBlockedException(Throwable cause) {
        super(cause);
    }
}
