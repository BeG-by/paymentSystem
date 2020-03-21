package by.beg.payment_system.exception.wallet_exception;

public class WalletIsExistException extends Exception {

    public WalletIsExistException() {
        super();
    }

    public WalletIsExistException(String message) {
        super(message);
    }

    public WalletIsExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public WalletIsExistException(Throwable cause) {
        super(cause);
    }
}
