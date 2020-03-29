package by.beg.payment_system.exception;

public class CreditIsPresentException extends Exception {

    public CreditIsPresentException() {
    }

    public CreditIsPresentException(String message) {
        super(message);
    }

    public CreditIsPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditIsPresentException(Throwable cause) {
        super(cause);
    }

}
