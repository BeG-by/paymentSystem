package by.beg.payment_system.exception.wallet_exception;

public class WalletNotFoundException extends Exception {

    public WalletNotFoundException() {
        super();
    }

    public WalletNotFoundException(String message) {
        super(message);
    }

    public WalletNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletNotFoundException(Throwable cause) {
        super(cause);
    }
}
