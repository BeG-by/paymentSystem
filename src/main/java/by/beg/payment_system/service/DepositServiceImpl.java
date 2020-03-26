package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.enumerations.DepositStatus;
import by.beg.payment_system.model.finance.enumerations.DepositType;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.util.CurrencyConverter;
import by.beg.payment_system.service.util.DepositCalculate;
import by.beg.payment_system.service.util.DepositFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DepositServiceImpl implements DepositService {

    private DepositRepository depositRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;
    private CurrencyConverter currencyConverter;


    public DepositServiceImpl(DepositRepository depositRepository, WalletRepository walletRepository, UserRepository userRepository, CurrencyConverter currencyConverter) {
        this.depositRepository = depositRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currencyConverter = currencyConverter;
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

        BigDecimal receivedMoney = currencyConverter.convertMoney(openDTO.getCurrencyType(), deposit.getCurrencyType(), moneySend);

        deposit.setUser(user);
        deposit.setBalance(receivedMoney);
        deposit.setReturnBalance(DepositCalculate.calculate(deposit));
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
        List<Deposit> allByUser = depositRepository.findAllByUser(user).stream().
                filter(deposit -> !deposit.getDepositStatus().equals(DepositStatus.DELETED)).collect(Collectors.toList());

        Date today = new Date();

        for (Deposit deposit : allByUser) {
            if (deposit.getFinishDate().before(today)) {
                deposit.setDepositStatus(DepositStatus.AVAILABLE);

                log.info("Deposit change status: " + deposit);
            }
        }

        return allByUser;
    }

    @Override
    public List<Deposit> pickUp(User user) throws WalletNotFoundException {
        List<Deposit> allByUser = depositRepository.findAllByUser(user).stream().
                filter(deposit -> deposit.getDepositStatus().equals(DepositStatus.AVAILABLE)).collect(Collectors.toList());
        ;

        for (Deposit deposit : allByUser) {
            Wallet wallet = walletRepository.findByCurrencyTypeAndUser(deposit.getCurrencyType(), user).orElseThrow(WalletNotFoundException::new);
            wallet.setBalance(wallet.getBalance().add(deposit.getReturnBalance()));
            deposit.setDepositStatus(DepositStatus.DELETED);

            log.info("Deposit change status: " + deposit);
        }

        return allByUser;
    }

    @Override
    public List<Deposit> getAllForAdmin(long userId) {
        return depositRepository.findAllByUser(userRepository.getOne(userId));
    }

    @Override
    public List<Deposit> getAllForAdminByStatus(DepositStatus depositStatus) {
        return depositRepository.findAllByDepositStatus(depositStatus).stream()
                .filter(deposit -> deposit.getDepositStatus().equals(depositStatus)).collect(Collectors.toList());
    }

    @Override
    public List<Deposit> filterByCreateDate(Date firstDate, Date secondDate) {
        return depositRepository.filterByCreateDate(firstDate , secondDate, Sort.by("startDate"));
    }

}
