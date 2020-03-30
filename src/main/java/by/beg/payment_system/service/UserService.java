package by.beg.payment_system.service;

import by.beg.payment_system.dto.UserAuthorizationDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.security.Token;
import by.beg.payment_system.model.user.User;

import java.util.List;

public interface UserService {

    User registration(User user) throws UserIsPresentException;

    Token authorization(UserAuthorizationDTO user) throws UserNotFoundException;

    User logout(User user);

    User checkAuthorization(String token) throws UserIsNotAuthorizedException, UserBlockedException;

    User findById(long id) throws UserNotFoundException;

    User findByEmail(String email) throws UserNotFoundException;

    User findByPassport(String passport) throws UserNotFoundException;

    User updateUser(User user) throws UserNotFoundException;

    User deleteUser(long userId) throws UserNotFoundException, UnremovableStatusException;

    List<User> getAllUsers();

    User getAdminRole(long userId) throws UserNotFoundException;

    User findByWalletValue(String walletValue) throws WalletNotFoundException, UserNotFoundException;

    User changeStatus(long userId, Status status) throws UserNotFoundException;

    void clearTokens();


}
