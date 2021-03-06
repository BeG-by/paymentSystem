package by.beg.payment_system.service.impl;

import by.beg.payment_system.dto.request.DepositOpenRequestDTO;
import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.NotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.DepositDetailRepository;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.DepositDetailService;
import by.beg.payment_system.service.util.CurrencyConverter;
import by.beg.payment_system.service.util.DepositDetailFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static by.beg.payment_system.service.util.MessageConstant.*;

@Service
@Transactional
@Slf4j
public class DepositDetailServiceImpl implements DepositDetailService {

    private DepositDetailRepository depositDetailRepository;
    private DepositRepository depositRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;
    private CurrencyConverter currencyConverter;


    @Autowired
    public DepositDetailServiceImpl(DepositDetailRepository depositDetailRepository,
                                    DepositRepository depositRepository,
                                    WalletRepository walletRepository,
                                    UserRepository userRepository,
                                    CurrencyConverter currencyConverter) {

        this.depositDetailRepository = depositDetailRepository;
        this.depositRepository = depositRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public void create(DepositOpenRequestDTO openDTO, User user)
            throws LackOfMoneyException, CurrencyConverterException {

        BigDecimal moneySend = openDTO.getMoney();
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(openDTO.getCurrencyType(), user)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

        if (wallet.getBalance().compareTo(moneySend) < 0) {
            throw new LackOfMoneyException();
        }

        Deposit deposit = depositRepository.findByName(openDTO.getDepositName())
                .filter(d -> d.getStatus().equals(Status.OPEN)).orElseThrow(() -> new NotFoundException(DEPOSIT_NOT_FOUND_MESSAGE));

        wallet.setBalance(wallet.getBalance().subtract(moneySend));
        BigDecimal receivedMoney = currencyConverter.convertMoney(openDTO.getCurrencyType(), deposit.getCurrencyType(), moneySend);
        DepositDetail depositDetail = DepositDetailFactory.createInstance(deposit, receivedMoney);
        depositDetail.setUser(user);

        DepositDetail depositDetailSave = depositDetailRepository.save(depositDetail);
        log.info("DepositDetail was created: " + depositDetailSave);

    }


    @Override
    public List<DepositDetail> findAllByUser(User user) {
        List<DepositDetail> depositDetails = depositDetailRepository.findAllByUser(user)
                .stream()
                .filter(deposit -> deposit.getDepositDetailStatus().equals(Status.OPEN))
                .filter(deposit -> deposit.getFinishDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        depositDetails.forEach(deposit -> {
            deposit.setDepositDetailStatus(Status.PRE_CLOSED);
            log.info("DepositDetail was changed status: " + deposit);
        });

        return depositDetails;
    }

    @Override
    public List<DepositDetail> pickUp(User user) {
        List<DepositDetail> depositDetails = depositDetailRepository.findAllByUser(user)
                .stream()
                .filter(deposit -> deposit.getDepositDetailStatus().equals(Status.PRE_CLOSED))
                .collect(Collectors.toList());

        for (DepositDetail depositDetail : depositDetails) {
            Wallet wallet = walletRepository.findByCurrencyTypeAndUser(depositDetail.getDeposit().getCurrencyType(), user)
                    .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

            wallet.setBalance(wallet.getBalance().add(depositDetail.getReturnBalance()));
            depositDetail.setDepositDetailStatus(Status.CLOSED);
            log.info("DepositDetail was changed status: " + depositDetail);
        }

        return depositDetails;
    }

    @Override
    public List<DepositDetail> findAllById(long userId) {
        return depositDetailRepository.findAllByUser(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE)));
    }

    @Override
    public List<DepositDetail> findAllByStatus(Status status) {
        return depositDetailRepository.findAllByDepositDetailStatus(status);
    }

    @Override
    public List<DepositDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate) {
        return depositDetailRepository.findAllByCreateDate(firstDate, secondDate, Sort.by("startDate"));
    }

    @Override
    public void deleteById(long depositId) {

        DepositDetail depositDetail = depositDetailRepository.findById(depositId)
                .orElseThrow(() -> new NotFoundException("Deposit detail not found"));

        if (depositDetail.getDepositDetailStatus().equals(Status.CLOSED)) {
            depositDetailRepository.delete(depositDetail);
            log.info("DepositDetail was deleted: " + depositDetail);
        } else {
            throw new UnremovableStatusException();
        }

    }

    @Override
    public List<DepositDetail> deleteAll() {
        List<DepositDetail> depositDetails = depositDetailRepository.deleteAllByDepositDetailStatus(Status.CLOSED);
        depositDetails.forEach(depositDetail -> log.info("DepositDetail's status was changed: " + depositDetail));
        return depositDetails;
    }

    @Override
    public void refreshAll() {
        List<DepositDetail> depositDetails = depositDetailRepository.findAll()
                .stream()
                .filter(deposit -> deposit.getDepositDetailStatus().equals(Status.OPEN))
                .filter(deposit -> deposit.getFinishDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        depositDetails.forEach(deposit -> {
            deposit.setDepositDetailStatus(Status.PRE_CLOSED);
            log.info("DepositDetail was changed status: " + deposit);
        });

    }

}
