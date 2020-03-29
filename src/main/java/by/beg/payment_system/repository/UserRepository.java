package by.beg.payment_system.repository;


import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.security.Token;
import by.beg.payment_system.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailOrPassport(String email, String passport);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByTokens(Token token);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPassport(String passport);

    Optional<User> findUserByWallets(Wallet wallet);

    Optional<User> findUserByCreditDetails(CreditDetail creditDetail);

}
