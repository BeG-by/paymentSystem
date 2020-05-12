package by.beg.payment_system.service.impl;

import by.beg.payment_system.exception.*;
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

import static by.beg.payment_system.service.util.MessageConstant.*;

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
    public Wallet create(CurrencyType type, User user) {

        if (walletRepository.findByCurrencyTypeAndUser(type, user).isPresent()) {
            throw new ExistException(WALLET_EXIST_MESSAGE);
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
    public void delete(CurrencyType type, User user) {

        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

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
    public Wallet recharge(User user, CurrencyType type, BigDecimal money) {
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

        wallet.setBalance(wallet.getBalance().add(money));
        log.info("User has charged the balance: " + wallet);
        return wallet;
    }

    @Override
    public void clear(CurrencyType type, User user) {
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(type, user)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

        wallet.setBalance(BigDecimal.ZERO);
        log.info("User has cleared the balance: " + wallet);
    }

}
