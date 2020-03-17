package by.beg.payment_system.service;

import by.beg.payment_system.model.User;
import by.beg.payment_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registration(User user) {
        final User save = userRepository.save(user);
        return save;
    }

    @Override
    public User authorization(User user) {
        return null;
    }
}
