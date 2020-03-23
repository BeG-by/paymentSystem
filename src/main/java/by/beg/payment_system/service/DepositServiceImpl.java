package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.Deposit;
import by.beg.payment_system.model.DepositType;
import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.util.CurrencyConverterUtil;
import by.beg.payment_system.util.DepositFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class DepositServiceImpl implements DepositService {

    private DepositRepository depositRepository;
    private WalletRepository walletRepository;
    private CurrencyConverterUtil currencyConverterUtil;


    public DepositServiceImpl(DepositRepository depositRepository, WalletRepository walletRepository, CurrencyConverterUtil currencyConverterUtil) {
        this.depositRepository = depositRepository;
        this.walletRepository = walletRepository;
        this.currencyConverterUtil = currencyConverterUtil;
    }

    @Override
    public Deposit create(DepositOpenDTO openDTO, User user) throws WalletNotFoundException, LackOfMoneyException {

        BigDecimal moneySend = openDTO.getMoney();
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(openDTO.getCurrencyType(), user).orElseThrow(WalletNotFoundException::new);

        if (wallet.getBalance().compareTo(moneySend) < 0) {throw new LackOfMoneyException();}

        wallet.setBalance(wallet.getBalance().subtract(moneySend));

        Deposit deposit = DepositFactory.getInstance(openDTO.getDepositType());

        BigDecimal receivedMoney = currencyConverterUtil.convertMoney(openDTO.getCurrencyType(), deposit.getCurrencyType(), moneySend);

        deposit.setUser(user);
        deposit.setBalance(receivedMoney);
        Deposit depositSave = depositRepository.save(deposit);
        log.info("Deposit was created: " + depositSave);

        return depositSave;
    }

    @Override
    public List<Deposit> getDepositDescription() {
        List<Deposit> deposits = new ArrayList<>();

        for (DepositType currentType : DepositType.values()) {
            deposits.add(DepositFactory.getInstance(currentType));
        }

        return deposits;
    }
}
