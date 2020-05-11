package by.beg.payment_system.service.impl;

import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.WalletService;
import by.beg.payment_system.util.GenerateWalletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@Transactional
public class WalletServiceImpl implements WalletService {

    private WalletRepository walletRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet create(CurrencyType type, User user) throws WalletIsExistException {

        if (walletRepository.findByCurrencyTypeAndUser(type, user).isPresent()) {
            throw new WalletIsExistException();
        }

        String walletValue = type.toString() + GenerateWalletUtil.generateWalletValue();

        Wallet save = walletRepository.save(new Wallet(walletValue, type, user));
        log.info("Wallet was added: " + save);
        return save;
    }

    @Override
    public List<Wallet> findAll(User user) {
        return walletRepository.findAllByUser(user);
    }


    @Override
    public void delete(CurrencyType type, User user) throws WalletNotFoundException, UnremovableStatusException {

        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user).orElseThrow(WalletNotFoundException::new);

        long depositCount = user.getDepositDetails().stream().
                filter(depositDetail -> !depositDetail.getDepositDetailStatus().equals(Status.CLOSED)
                        && depositDetail.getDeposit().getCurrencyType().equals(type)).count();

        long creditCount = user.getCreditDetails().stream()
                .filter(creditDetail -> !creditDetail.getCreditStatus().equals(Status.CLOSED)
                        && creditDetail.getCredit().getCurrencyType().equals(type)).count();

        if (depositCount > 0 || creditCount > 0 || wallet.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new UnremovableStatusException();
        }

        walletRepository.delete(wallet);
        log.info("Wallet was deleted: " + wallet);
    }

    @Override
    public Wallet recharge(User user, CurrencyType type, BigDecimal money) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user).orElseThrow(WalletNotFoundException::new);
        wallet.setBalance(wallet.getBalance().add(money));
        log.info("User has charged the balance: " + wallet);
        return wallet;
    }

    @Override
    public void clear(CurrencyType type, User user) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user).orElseThrow(WalletNotFoundException::new);
        wallet.setBalance(BigDecimal.ZERO);
        log.info("User has cleared the balance: " + wallet);
    }

}
