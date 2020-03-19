package by.beg.payment_system.repository;


import by.beg.payment_system.model.Token;
import by.beg.payment_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailOrPassport(String email, String passport);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByTokens(Token token);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPassport(String passport);

}
