package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositStatus;
import by.beg.payment_system.model.finance.DepositType;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.util.CurrencyConverterUtil;
import by.beg.payment_system.util.DepositCalculateUtil;
import by.beg.payment_system.util.DepositFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        if (wallet.getBalance().compareTo(moneySend) < 0) {
            throw new LackOfMoneyException();
        }

        wallet.setBalance(wallet.getBalance().subtract(moneySend));

        Deposit deposit = DepositFactory.getInstance(openDTO.getDepositType());

        BigDecimal receivedMoney = currencyConverterUtil.convertMoney(openDTO.getCurrencyType(), deposit.getCurrencyType(), moneySend);

        deposit.setUser(user);
        deposit.setBalance(receivedMoney);
        deposit.setReturnBalance(DepositCalculateUtil.calculate(deposit));
        Deposit depositSave = depositRepository.save(deposit);
        log.info("Deposit was created: " + depositSave);

        return depositSave;
    }

    @Override
    public List<Deposit> getDepositsDescription() {
        List<Deposit> deposits = new ArrayList<>();

        for (DepositType currentType : DepositType.values()) {
            deposits.add(DepositFactory.getInstance(currentType));
        }

        return deposits;
    }

    @Override
    public List<Deposit> getAllByUser(User user) {
        List<Deposit> allByUser = depositRepository.findAllByUser(user);

        Date today = new Date();

        for (Deposit deposit : allByUser) {
            if (deposit.getFinishDate().before(today)) {
                deposit.setDepositStatus(DepositStatus.AVAILABLE);
            }
        }

        return allByUser;
    }
}
