package by.beg.payment_system.exception;

public class CreditDetailIsPresentException extends Exception {

    public CreditDetailIsPresentException() {
    }

    public CreditDetailIsPresentException(String message) {
        super(message);
    }

    public CreditDetailIsPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreditDetailIsPresentException(Throwable cause) {
        super(cause);
    }
}
