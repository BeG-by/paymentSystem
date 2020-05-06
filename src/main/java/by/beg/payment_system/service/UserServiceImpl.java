package by.beg.payment_system.service;

import by.beg.payment_system.dto.UserAuthorizationDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.security.Token;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.TokenRepository;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, WalletRepository walletRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.tokenRepository = tokenRepository;
    }


    @Override
    public User registration(User user) throws UserIsPresentException {

        if (userRepository.findUserByEmailOrPassport(user.getEmail(), user.getPassport()).isPresent()) {
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
    public User logout(User user) {
        List<Token> allByUser = tokenRepository.findAllByUser(user);
        tokenRepository.deleteAll(allByUser);
        allByUser.forEach(token -> log.info("Token was deleted: " + token));
        return user;
    }

    @Override
    public User checkAuthorization(String token) throws UserIsNotAuthorizedException, UserBlockedException {
        Token tokenByTokenValue = tokenRepository.findTokenByTokenValue(token).orElseThrow(UserIsNotAuthorizedException::new);
        User user = userRepository.findUserByTokens(tokenByTokenValue).orElseThrow(UserIsNotAuthorizedException::new);
        if (user.getStatus().equals(Status.BLOCKED)) {
            throw new UserBlockedException();
        }
        return user;
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
    public User deleteUser(long userId) throws UserNotFoundException, UnremovableStatusException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        long walletCount = user.getWallets().stream().filter(wallet -> wallet.getBalance().compareTo(new BigDecimal(0)) > 0).count();
        long depositCount = user.getDepositDetails().stream().filter(depositDetail -> !depositDetail.getDepositDetailStatus().equals(Status.DELETED)).count();
        long creditCount = user.getCreditDetails().stream().filter(creditDetail -> !creditDetail.getCreditStatus().equals(Status.CLOSED)).count();

        if (walletCount > 0 || depositCount > 0 || creditCount > 0) {
            throw new UnremovableStatusException();
        }

        user.getTransferDetails().forEach(transferDetail -> transferDetail.setUser(null));

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
        user.setUserRole(UserRole.ADMIN);
        user.setLastUpdate(LocalDateTime.now());
        log.info("Admin role was added for: " + user);
        return user;
    }

    @Override
    public User findByWalletValue(String walletValue) throws WalletNotFoundException, UserNotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletValue(walletValue).orElseThrow(WalletNotFoundException::new);
        return userRepository.findUserByWallets(wallet).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User changeStatus(long userId, Status status) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setStatus(status);
        user.setLastUpdate(LocalDateTime.now());
        log.info("Status was changed for: " + user);
        return user;
    }

    @Override
    public void clearTokens() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        List<Token> tokens = tokenRepository.findAll();

        for (Token currentToken : tokens) {
            calendar.setTime(currentToken.getCreateDate());
            calendar.add(Calendar.MINUTE, 15);

            if (today.after(calendar.getTime())) {
                tokenRepository.delete(currentToken);
                log.info("Token was deleted: " + currentToken);
            }

        }

    }


}
