package by.beg.payment_system.repository;

import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByWalletTypeAndUser(Wallet.WalletType walletType, User user);
}
