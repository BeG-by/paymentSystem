package by.beg.payment_system.service;

import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.transfer_exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.TransferDetail;
import by.beg.payment_system.model.User;

import java.util.Map;

public interface TransferService {

    TransferDetail doTransfer(User user, TransferDetail transferDetail) throws WalletNotFoundException, LackOfMoneyException, TargetWalletNotFoundException;

    Map<String, Double> getExchangeRates();

}
