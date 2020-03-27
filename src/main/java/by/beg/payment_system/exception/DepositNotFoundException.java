package by.beg.payment_system.exception;

public class DepositNotFoundException extends Exception {

    public DepositNotFoundException() {
    }

    public DepositNotFoundException(String message) {
        super(message);
    }

    public DepositNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepositNotFoundException(Throwable cause) {
        super(cause);
    }
}
