package by.beg.payment_system.service;

import by.beg.payment_system.dto.UserAuthorizationDTO;
import by.beg.payment_system.exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.model.Token;
import by.beg.payment_system.model.User;

import java.util.List;

public interface UserService {

    User registration(User user) throws UserIsPresentException;

    Token authorization(UserAuthorizationDTO user) throws UserNotFoundException;

    User logout(String token) throws UserNotFoundException, UserIsNotAuthorizedException;

    User checkAuthorization(String token) throws UserIsNotAuthorizedException;

    User findById(long id) throws UserNotFoundException;

    User findByEmail(String email) throws UserNotFoundException;

    User findByPassport(String passport) throws UserNotFoundException;

    User updateUser(User user) throws UserNotFoundException;

    User deleteUser(long userId) throws UserNotFoundException;

    List<User> getAllUsers();

    User getAdminRole(long userId) throws UserNotFoundException;


}
