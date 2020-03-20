package by.beg.payment_system.service;

import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;

public interface WalletService {

    Wallet create(Wallet.WalletType type, User user) throws WalletIsExistException;

}
