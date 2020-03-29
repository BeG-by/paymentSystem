package by.beg.payment_system.service;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransferService {

    TransferDetail doTransfer(User user, TransferDetail transferDetail) throws WalletNotFoundException, LackOfMoneyException, TargetWalletNotFoundException, CurrencyConverterException;

    Map<String, BigDecimal> getExchangeRates() throws CurrencyConverterException;

    List<TransferDetail> filterByDate(Date firstDate, Date secondDate);

    List<TransferDetail> getAll();

    TransferDetail deleteById(long id) throws TransferNotFoundException;

    List<TransferDetail> deleteBetweenDate(Date firstDate, Date secondDate);

}
