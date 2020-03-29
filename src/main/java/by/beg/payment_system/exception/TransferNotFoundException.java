package by.beg.payment_system.exception;

public class TransferNotFoundException extends Exception{

    public TransferNotFoundException() {
    }

    public TransferNotFoundException(String message) {
        super(message);
    }

    public TransferNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransferNotFoundException(Throwable cause) {
        super(cause);
    }
}
