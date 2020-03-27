package by.beg.payment_system.service;

import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.finance.enumerations.CurrencyType;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@Transactional
public class WalletServiceImpl implements WalletService {

    private WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet create(CurrencyType type, User user) throws WalletIsExistException {

        if (walletRepository.findByCurrencyTypeAndUser(type, user).isPresent()) {
            throw new WalletIsExistException();
        }

        String walletValue = type.toString() + GenerateUtil.generateWalletValue();

        Wallet save = walletRepository.save(new Wallet(walletValue, type, user));
        log.info("Wallet was added: " + save);
        return save;
    }

    @Override
    public List<Wallet> getAll(User user) {
        return walletRepository.findAllByUser(user);
    }

    @Override
    public Wallet delete(CurrencyType type, User user) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user).orElseThrow(WalletNotFoundException::new);
        walletRepository.delete(wallet);
        log.info("Wallet was deleted: " + wallet);
        return wallet;
    }

    @Override
    public Wallet recharge(User user, CurrencyType type, BigDecimal money) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user).orElseThrow(WalletNotFoundException::new);
        wallet.setBalance(wallet.getBalance().add(money));
        log.info("User has charged the balance: " + wallet);
        return wallet;
    }

}
