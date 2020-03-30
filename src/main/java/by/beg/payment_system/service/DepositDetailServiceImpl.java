package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.DepositDetailRepository;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.util.CurrencyConverter;
import by.beg.payment_system.service.util.DepositDetailFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DepositDetailServiceImpl implements DepositDetailService {

    private DepositDetailRepository depositDetailRepository;
    private DepositRepository depositRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;
    private CurrencyConverter currencyConverter;


    public DepositDetailServiceImpl(DepositDetailRepository depositDetailRepository, DepositRepository depositRepository,
                                    WalletRepository walletRepository, UserRepository userRepository, CurrencyConverter currencyConverter) {
        this.depositDetailRepository = depositDetailRepository;
        this.depositRepository = depositRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public DepositDetail create(DepositOpenDTO openDTO, User user)
            throws WalletNotFoundException, LackOfMoneyException, DepositNotFoundException, CurrencyConverterException {

        BigDecimal moneySend = openDTO.getMoney();
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(openDTO.getCurrencyType(), user).orElseThrow(WalletNotFoundException::new);

        if (wallet.getBalance().compareTo(moneySend) < 0) {
            throw new LackOfMoneyException();
        }

        Deposit deposit = depositRepository.findByName(openDTO.getDepositName()).
                filter(d -> d.getStatus().equals(Status.AVAILABLE)).orElseThrow(DepositNotFoundException::new);

        wallet.setBalance(wallet.getBalance().subtract(moneySend));
        BigDecimal receivedMoney = currencyConverter.convertMoney(openDTO.getCurrencyType(), deposit.getCurrencyType(), moneySend);
        DepositDetail depositDetail = DepositDetailFactory.getInstance(deposit, receivedMoney);
        depositDetail.setUser(user);

        DepositDetail depositDetailSave = depositDetailRepository.save(depositDetail);
        log.info("DepositDetail was created: " + depositDetailSave);

        return depositDetailSave;
    }


    @Override
    public List<DepositDetail> getAllByUser(User user) {
        List<DepositDetail> allByUser = depositDetailRepository.findAllByUser(user).stream().
                filter(deposit -> !deposit.getDepositDetailStatus().equals(Status.DELETED)).collect(Collectors.toList());

        Date today = new Date();

        for (DepositDetail depositDetail : allByUser) {
            if (depositDetail.getFinishDate().before(today)) {
                depositDetail.setDepositDetailStatus(Status.AVAILABLE);
                log.info("DepositDetail was changed status: " + depositDetail);
            }
        }

        return allByUser;
    }

    @Override
    public List<DepositDetail> pickUp(User user) throws WalletNotFoundException {
        List<DepositDetail> allByUser = depositDetailRepository.findAllByUser(user).stream().
                filter(deposit -> deposit.getDepositDetailStatus().equals(Status.AVAILABLE)).collect(Collectors.toList());


        for (DepositDetail depositDetail : allByUser) {
            Wallet wallet = walletRepository.findByCurrencyTypeAndUser(depositDetail.getDeposit().getCurrencyType(), user).orElseThrow(WalletNotFoundException::new);
            wallet.setBalance(wallet.getBalance().add(depositDetail.getReturnBalance()));
            depositDetail.setDepositDetailStatus(Status.DELETED);
            log.info("DepositDetail was changed status: " + depositDetail);
        }

        return allByUser;
    }

    @Override
    public List<DepositDetail> getAllForAdmin(long userId) throws UserNotFoundException {
        return depositDetailRepository.findAllByUser(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public List<DepositDetail> getAllForAdminByStatus(Status status) {
        return depositDetailRepository.findAllByDepositDetailStatus(status);
    }

    @Override
    public List<DepositDetail> filterByCreateDate(Date firstDate, Date secondDate) {
        return depositDetailRepository.filterByCreateDate(firstDate, secondDate, Sort.by("startDate"));
    }

    @Override
    public DepositDetail delete(long depositId) throws DepositNotFoundException, UnremovableStatusException {

        DepositDetail depositDetail = depositDetailRepository.findById(depositId).orElseThrow(DepositNotFoundException::new);

        if (depositDetail.getDepositDetailStatus().equals(Status.DELETED)) {
            depositDetailRepository.delete(depositDetail);
            log.info("DepositDetail was deleted: " + depositDetail);
        } else {
            throw new UnremovableStatusException();
        }

        return depositDetail;
    }

    @Override
    public List<DepositDetail> deleteAll() {
        List<DepositDetail> depositDetails = depositDetailRepository.deleteAllByDepositDetailStatus(Status.DELETED);
        depositDetails.forEach(depositDetail -> log.info("DepositDetail's status was changed: " + depositDetail));
        return depositDetails;
    }

    @Override
    public void refreshAll() {
        List<DepositDetail> depositDetails = depositDetailRepository.findAll().stream()
                .filter(depositDetail -> !depositDetail.getDepositDetailStatus().equals(Status.DELETED)).collect(Collectors.toList());

        Date today = new Date();

        for (DepositDetail depositDetail : depositDetails) {
            if (depositDetail.getFinishDate().before(today)) {
                depositDetail.setDepositDetailStatus(Status.AVAILABLE);
                log.info("DepositDetail's status was changed: " + depositDetail);
            }
        }

    }

}
