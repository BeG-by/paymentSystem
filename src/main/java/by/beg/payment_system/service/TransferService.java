package by.beg.payment_system.service;

import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransferService {

    TransferDetail doTransfer(User user, TransferDetail transferDetail) throws WalletNotFoundException, LackOfMoneyException, TargetWalletNotFoundException;

    Map<String, Double> getExchangeRates();

    List<TransferDetail> filterByDate(Date firstDate, Date secondDate);

}
