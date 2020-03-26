package by.beg.payment_system.service;

import by.beg.payment_system.dto.UserAuthorizationDTO;
import by.beg.payment_system.exception.user_exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.user_exception.UserIsPresentException;
import by.beg.payment_system.exception.user_exception.UserNotFoundException;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.security.Token;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.TokenRepository;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    @Override
    public User registration(User user) throws UserIsPresentException {
        Optional<User> checkUser = userRepository.findUserByEmailOrPassport(user.getEmail(), user.getPassport());
        if (checkUser.isPresent()) {
            throw new UserIsPresentException();
        }

        User saveUser = userRepository.save(user);
        log.info("User was added: " + saveUser);
        return saveUser;
    }

    @Override
    public Token authorization(UserAuthorizationDTO user) throws UserNotFoundException {
        User checkUser = userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword()).orElseThrow(UserNotFoundException::new);

        String tokenValue = GenerateUtil.generateToken();
        Token token = tokenRepository.save(new Token(tokenValue, checkUser));
        log.info("Token was added: " + token);
        return token;
    }

    @Override
    public User logout(String token) throws UserNotFoundException, UserIsNotAuthorizedException {
        Optional<Token> tokenByTokenValue = tokenRepository.findTokenByTokenValue(token);
        Optional<User> userByTokens = userRepository.findUserByTokens(tokenByTokenValue.orElseThrow(UserIsNotAuthorizedException::new));
        List<Token> allByUser = tokenRepository.findAllByUser(userByTokens.orElseThrow(UserNotFoundException::new));

        tokenRepository.deleteAll(allByUser);

        for (Token currentToken : allByUser) {
            log.info("Token was deleted: " + currentToken);
        }

        return userByTokens.get();
    }

    @Override
    public User checkAuthorization(String token) throws UserIsNotAuthorizedException {
        Token tokenByTokenValue = tokenRepository.findTokenByTokenValue(token).orElseThrow(UserIsNotAuthorizedException::new);
        return userRepository.findUserByTokens(tokenByTokenValue).orElseThrow(UserIsNotAuthorizedException::new);
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByPassport(String passport) throws UserNotFoundException {
        return userRepository.findUserByPassport(passport).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        User persistUser = userRepository.findById(user.getId()).map(currentUser -> userRepository.save(user)).orElseThrow(UserNotFoundException::new);
        log.info("User was updated: " + persistUser);
        return persistUser;
    }

    @Override
    public User deleteUser(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        for (TransferDetail detail : user.getTransferDetails()) {
            detail.setUser(null);
        }

        userRepository.delete(user);

        log.info("User was deleted: " + user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getAdminRole(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setUserRole(User.UserRole.ADMIN);
        user.setLastUpdate(new Date());
        log.info("Admin role was added for user: " + user);
        return user;
    }


}
