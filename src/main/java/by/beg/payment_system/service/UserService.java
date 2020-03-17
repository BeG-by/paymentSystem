package by.beg.payment_system.service;

import by.beg.payment_system.model.User;

public interface UserService {

    User registration(User user);

    User authorization(User user);

}
