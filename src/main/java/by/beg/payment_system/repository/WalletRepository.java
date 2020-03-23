package by.beg.payment_system.repository;

import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;
import by.beg.payment_system.model.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByCurrencyTypeAndUser(CurrencyType currencyType, User user);

    List<Wallet> findAllByUser(User user);

    Optional<Wallet> findWalletByWalletValue(String walletValue);

}
