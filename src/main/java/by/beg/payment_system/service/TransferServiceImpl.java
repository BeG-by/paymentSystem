package by.beg.payment_system.service;

import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.transfer_exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.TransferDetail;
import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;
import by.beg.payment_system.model.CurrencyType;
import by.beg.payment_system.repository.TransferRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.util.CurrencyConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;


@Service
@Transactional
@Slf4j
public class TransferServiceImpl implements TransferService {

    private TransferRepository transferRepository;
    private WalletRepository walletRepository;
    private CurrencyConverterUtil currencyConverterUtil;

    public TransferServiceImpl(TransferRepository transferRepository, WalletRepository walletRepository, CurrencyConverterUtil currencyConverterUtil) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
        this.currencyConverterUtil = currencyConverterUtil;
    }

    @Override
    public TransferDetail doTransfer(User user, TransferDetail transferDetail) throws WalletNotFoundException, LackOfMoneyException, TargetWalletNotFoundException {

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

        if (userWallet == null) {throw new WalletNotFoundException();}
        if (userWallet.getBalance().compareTo(moneySend) < 0) {throw new LackOfMoneyException();}

        Wallet targetWallet = walletRepository.findWalletByWalletValue(targetWalletValue).orElseThrow(TargetWalletNotFoundException::new);

        userWallet.setBalance(userWallet.getBalance().subtract(moneySend));
        BigDecimal receivedMoney = currencyConverterUtil.convertMoney(userWallet.getCurrencyType(), targetWallet.getCurrencyType(), moneySend);
        targetWallet.setBalance(targetWallet.getBalance().add(receivedMoney));

        transferDetail.setUser(user);
        transferDetail.setMoneyReceive(receivedMoney);
        TransferDetail transferSave = transferRepository.save(transferDetail);
        log.info("Transfer was added: " + transferSave);

        return transferSave;
    }

    @Override
    public Map<String, Double> getExchangeRates() {
        return currencyConverterUtil.getExchangeRates();
    }


}
