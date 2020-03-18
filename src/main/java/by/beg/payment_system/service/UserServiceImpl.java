package by.beg.payment_system.service;

import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.model.User;
import by.beg.payment_system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registration(User user) throws UserIsPresentException {
        Optional<User> checkUser = userRepository.findUserByEmailOrPassport(user.getEmail(), user.getPassport());
        if (checkUser.isPresent()) {
            throw new UserIsPresentException();
        }

        final User saveUser = userRepository.save(user);
        log.info("User was added " + saveUser);
        return saveUser;
    }

    @Override
    public User authorization(User user) {
        return null;
    }
}
