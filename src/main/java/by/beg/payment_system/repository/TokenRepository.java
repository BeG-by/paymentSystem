package by.beg.payment_system.repository;

import by.beg.payment_system.model.security.Token;
import by.beg.payment_system.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findAllByUser(User user);

    Optional<Token> findTokenByTokenValue(String token);

}
