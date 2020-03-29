package by.beg.payment_system.exception;

public class CreditNotFoundException extends Exception {

    public CreditNotFoundException() {
    }

    public CreditNotFoundException(String message) {
        super(message);
    }

    public CreditNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditNotFoundException(Throwable cause) {
        super(cause);
    }
}
