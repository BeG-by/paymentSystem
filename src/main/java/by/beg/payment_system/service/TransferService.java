package by.beg.payment_system.service;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransferService {

    void makeTransfer(User user, TransferDetail transferDetail)
            throws WalletNotFoundException, LackOfMoneyException, TargetWalletNotFoundException, CurrencyConverterException;

    Map<String, BigDecimal> findAllRates() throws CurrencyConverterException;

    List<TransferDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

    List<TransferDetail> findAll();

    void deleteById(long id) throws TransferNotFoundException;

    List<TransferDetail> deleteBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

}
