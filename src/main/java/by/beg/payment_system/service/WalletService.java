package by.beg.payment_system.service;

import by.beg.payment_system.exception.wallet_exception.WalletIsExistException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.CurrencyType;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.finance.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet create(CurrencyType type, User user) throws WalletIsExistException;

    List<Wallet> getAll(User user);

    Wallet delete(CurrencyType type, User user) throws WalletNotFoundException;

    Wallet recharge(User user, CurrencyType type, BigDecimal balance) throws WalletNotFoundException;

}
