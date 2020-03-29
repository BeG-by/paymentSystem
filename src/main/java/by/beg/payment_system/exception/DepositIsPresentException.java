package by.beg.payment_system.exception;

public class DepositIsPresentException extends Exception {

    public DepositIsPresentException() {
    }

    public DepositIsPresentException(String message) {
        super(message);
    }

    public DepositIsPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepositIsPresentException(Throwable cause) {
        super(cause);
    }
}
