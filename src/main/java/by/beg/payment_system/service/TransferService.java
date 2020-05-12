package by.beg.payment_system.service;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransferService {

    void makeTransfer(User user, TransferDetail transferDetail) throws LackOfMoneyException, CurrencyConverterException;

    Map<String, BigDecimal> findAllRates() throws CurrencyConverterException;

    List<TransferDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

    List<TransferDetail> findAll();

    void deleteById(long id);

    List<TransferDetail> deleteBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

}
