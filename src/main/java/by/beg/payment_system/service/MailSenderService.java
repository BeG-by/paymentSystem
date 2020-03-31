package by.beg.payment_system.service;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserNotFoundException;

public interface MailSenderService {

    void sendExchangeRates(long userId) throws CurrencyConverterException, UserNotFoundException;

    void sendBlockNotification(long userId) throws UnremovableStatusException;
}
