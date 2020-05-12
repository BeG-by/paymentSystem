package by.beg.payment_system.service.impl;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.TransferRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.TransferService;
import by.beg.payment_system.service.util.CurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static by.beg.payment_system.service.util.MessageConstant.*;

@Service
@Transactional
@Slf4j
public class TransferServiceImpl implements TransferService {

    private TransferRepository transferRepository;
    private WalletRepository walletRepository;
    private CurrencyConverter currencyConverter;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository,
                               WalletRepository walletRepository,
                               CurrencyConverter currencyConverter) {

        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public void makeTransfer(User user, TransferDetail transferDetail) throws LackOfMoneyException, CurrencyConverterException {

        CurrencyType currencyType = transferDetail.getCurrencyType();
        BigDecimal moneySend = transferDetail.getMoneySend();
        String targetWalletValue = transferDetail.getTargetWalletValue();

        Wallet userWallet = null;

        for (Wallet currentWallet : user.getWallets()) {
            if (currentWallet.getCurrencyType().equals(currencyType)) {
                userWallet = currentWallet;
                break;
            }
        }

        if (userWallet == null) {
            throw new NotFoundException(WALLET_NOT_FOUND_MESSAGE);
        }

        if (userWallet.getBalance().compareTo(moneySend) < 0) {
            throw new LackOfMoneyException();
        }

        Wallet targetWallet = walletRepository.findWalletByWalletValue(targetWalletValue)
                .orElseThrow(() -> new NotFoundException("Target wallet not found"));

        userWallet.setBalance(userWallet.getBalance().subtract(moneySend));
        BigDecimal receivedMoney = currencyConverter.convertMoney(userWallet.getCurrencyType(), targetWallet.getCurrencyType(), moneySend);
        targetWallet.setBalance(targetWallet.getBalance().add(receivedMoney));

        transferDetail.setUser(user);
        transferDetail.setMoneyReceive(receivedMoney);
        TransferDetail transferSave = transferRepository.save(transferDetail);
        log.info("Transfer was made: " + transferSave);

    }

    @Override
    public Map<String, BigDecimal> findAllRates() throws CurrencyConverterException {
        return currencyConverter.receiveAllRates();
    }

    @Override
    public List<TransferDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate) {
        return transferRepository.findAllByDate(firstDate, secondDate, Sort.by("date"));
    }

    @Override
    public List<TransferDetail> findAll() {
        return transferRepository.findAll(Sort.by("date"));
    }

    @Override
    public void deleteById(long id) {
        TransferDetail transferDetail = transferRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TRANSFER_NOT_FOUND_MESSAGE));

        transferRepository.delete(transferDetail);
        log.info("TransferDetail was deleted: " + transferDetail);
    }

    @Override
    public List<TransferDetail> deleteBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate) {
        List<TransferDetail> transferDetails = transferRepository.findAllByDate(firstDate, secondDate, Sort.by("date"));
        transferRepository.deleteAll(transferDetails);
        transferDetails.forEach(transferDetail -> log.info("TransferDetail was deleted: " + transferDetail));
        return transferDetails;
    }

}
