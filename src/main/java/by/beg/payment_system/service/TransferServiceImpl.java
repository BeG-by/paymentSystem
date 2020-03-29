package by.beg.payment_system.service;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.TransferRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.util.CurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@Slf4j
public class TransferServiceImpl implements TransferService {

    private TransferRepository transferRepository;
    private WalletRepository walletRepository;
    private CurrencyConverter currencyConverter;

    public TransferServiceImpl(TransferRepository transferRepository, WalletRepository walletRepository, CurrencyConverter currencyConverter) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public TransferDetail doTransfer(User user, TransferDetail transferDetail)
            throws WalletNotFoundException, LackOfMoneyException, TargetWalletNotFoundException, CurrencyConverterException {

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
            throw new WalletNotFoundException();
        }

        if (userWallet.getBalance().compareTo(moneySend) < 0) {
            throw new LackOfMoneyException();
        }

        Wallet targetWallet = walletRepository.findWalletByWalletValue(targetWalletValue).orElseThrow(TargetWalletNotFoundException::new);

        userWallet.setBalance(userWallet.getBalance().subtract(moneySend));
        BigDecimal receivedMoney = currencyConverter.convertMoney(userWallet.getCurrencyType(), targetWallet.getCurrencyType(), moneySend);
        targetWallet.setBalance(targetWallet.getBalance().add(receivedMoney));

        transferDetail.setUser(user);
        transferDetail.setMoneyReceive(receivedMoney);
        TransferDetail transferSave = transferRepository.save(transferDetail);
        log.info("Transfer was added: " + transferSave);

        return transferSave;
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates() throws CurrencyConverterException {
        return currencyConverter.getAllRates();
    }

    @Override
    public List<TransferDetail> filterByDate(Date firstDate, Date secondDate) {
        return transferRepository.filterByDate(firstDate, secondDate, Sort.by("date"));
    }

    @Override
    public List<TransferDetail> getAll() {
        return transferRepository.findAll(Sort.by("date"));
    }

    @Override
    public TransferDetail deleteById(long id) throws TransferNotFoundException {
        TransferDetail transferDetail = transferRepository.findById(id).orElseThrow(TransferNotFoundException::new);
        transferRepository.delete(transferDetail);
        log.info("TransferDetail was deleted" + transferDetail);
        return transferDetail;
    }

    @Override
    public List<TransferDetail> deleteBetweenDate(Date firstDate, Date secondDate) {
        List<TransferDetail> transferDetails = transferRepository.filterByDate(firstDate, secondDate, Sort.by("date"));
        transferRepository.deleteAll(transferDetails);
        transferDetails.forEach(transferDetail -> log.info("TransferDetail was deleted" + transferDetail));
        return transferDetails;
    }


}
