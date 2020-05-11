package by.beg.payment_system.service;

import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet create(CurrencyType type, User user) throws WalletIsExistException;

    List<Wallet> findAll(User user);

    void delete(CurrencyType type, User user) throws WalletNotFoundException, UnremovableStatusException;

    Wallet recharge(User user, CurrencyType type, BigDecimal balance) throws WalletNotFoundException;

    void clear(CurrencyType type, User user) throws WalletNotFoundException;

}
