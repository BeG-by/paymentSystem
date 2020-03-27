package by.beg.payment_system.exception;

public class UserIsNotAuthorizedException extends Exception {

    public UserIsNotAuthorizedException() {
        super();
    }

    public UserIsNotAuthorizedException(String message) {
        super(message);
    }

    public UserIsNotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNotAuthorizedException(Throwable cause) {
        super(cause);
    }
}
