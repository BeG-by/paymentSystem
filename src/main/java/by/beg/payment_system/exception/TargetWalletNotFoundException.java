package by.beg.payment_system.exception;

public class TargetWalletNotFoundException extends Exception {

    public TargetWalletNotFoundException() {
        super();
    }

    public TargetWalletNotFoundException(String message) {
        super(message);
    }

    public TargetWalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TargetWalletNotFoundException(Throwable cause) {
        super(cause);
    }
}
