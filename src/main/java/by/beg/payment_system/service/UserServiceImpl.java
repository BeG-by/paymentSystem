package by.beg.payment_system.service;

import by.beg.payment_system.dto.UserAuthorizationDTO;
import by.beg.payment_system.exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.model.Token;
import by.beg.payment_system.model.User;
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

        final User saveUser = userRepository.save(user);
        log.info("User was added: " + saveUser);
        return saveUser;
    }

    @Override
    public Token authorization(UserAuthorizationDTO user) throws UserNotFoundException {
        Optional<User> checkUser = userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword());

        if (checkUser.isPresent()) {
            String tokenValue = GenerateUtil.generateToken();
            Token token = tokenRepository.save(new Token(tokenValue, checkUser.get()));
            log.info("Token was added: " + token);
            return token;
        }

        throw new UserNotFoundException();
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
        Optional<Token> tokenByTokenValue = tokenRepository.findTokenByTokenValue(token);
        Optional<User> userByTokens = userRepository.findUserByTokens(tokenByTokenValue.orElseThrow(UserIsNotAuthorizedException::new));
        return userByTokens.orElseThrow(UserIsNotAuthorizedException::new);
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
        userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        User userSave = userRepository.save(user);
        log.info("User was updated: " + userSave);
        return userSave;
    }

    @Override
    public User deleteUser(long userId) throws UserNotFoundException {
        Optional<User> byId = userRepository.findById(userId);
        userRepository.delete(byId.orElseThrow(UserNotFoundException::new));
        User user = byId.get();
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
        log.info("Admin role was added for user:" + user);
        return user;
    }


}
