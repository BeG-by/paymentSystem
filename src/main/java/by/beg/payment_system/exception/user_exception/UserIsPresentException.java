package by.beg.payment_system.exception.user_exception;

public class UserIsPresentException extends Exception {

    public UserIsPresentException() {
        super();
    }

    public UserIsPresentException(String message) {
        super(message);
    }

    public UserIsPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsPresentException(Throwable cause) {
        super(cause);
    }

}
