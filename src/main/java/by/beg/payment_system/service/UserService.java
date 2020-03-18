package by.beg.payment_system.service;

import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.model.User;

public interface UserService {

    User registration(User user) throws UserIsPresentException;

    User authorization(User user);

}
