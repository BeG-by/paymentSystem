package by.beg.payment_system.service;

import by.beg.payment_system.exception.CurrencyConverterException;

public interface MailSenderService {

    void sendExchangeRates(long userId) throws CurrencyConverterException;

    void sendBlockNotification(long userId);
}
