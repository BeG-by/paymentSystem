package by.beg.payment_system.exception;

public class LackOfMoneyException extends Exception{

    public LackOfMoneyException() {
        super();
    }

    public LackOfMoneyException(String message) {
        super(message);
    }

    public LackOfMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

    public LackOfMoneyException(Throwable cause) {
        super(cause);
    }
}
