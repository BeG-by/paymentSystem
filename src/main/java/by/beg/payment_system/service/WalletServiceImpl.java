package by.beg.payment_system.service;

import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class WalletServiceImpl implements WalletService {

    private UserRepository userRepository;
    private WalletRepository walletRepository;

    public WalletServiceImpl(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet create(Wallet.WalletType type, User user) throws WalletIsExistException {

        if (walletRepository.findByWalletTypeAndUser(type, user).isPresent()) {
            throw new WalletIsExistException();
        }

        String walletValue = type.toString() + GenerateUtil.generateWalletValue();

        Wallet save = walletRepository.save(new Wallet(walletValue, type, user));
        log.info("Wallet was added: " + save);
        return save;
    }
}
