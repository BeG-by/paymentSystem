package by.beg.payment_system.exception;

public class CurrencyConverterException extends Exception {

    public CurrencyConverterException() {
        super();
    }

    public CurrencyConverterException(String message) {
        super(message);
    }

    public CurrencyConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyConverterException(Throwable cause) {
        super(cause);
    }
}
