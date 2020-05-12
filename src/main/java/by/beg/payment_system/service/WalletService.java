package by.beg.payment_system.service;

import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    Wallet create(CurrencyType type, User user);

    List<Wallet> findAll(User user);

    void delete(CurrencyType type, User user);

    Wallet recharge(User user, CurrencyType type, BigDecimal balance);

    void clear(CurrencyType type, User user);

}
