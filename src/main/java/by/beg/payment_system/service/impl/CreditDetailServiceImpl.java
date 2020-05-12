package by.beg.payment_system.service.impl;

import by.beg.payment_system.dto.request.CreditOpenRequestDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.CreditDetailRepository;
import by.beg.payment_system.repository.CreditRepository;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.CreditDetailService;
import by.beg.payment_system.service.util.CreditDetailFactory;
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
public class CreditDetailServiceImpl implements CreditDetailService {

    private CreditDetailRepository creditDetailRepository;
    private CreditRepository creditRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;

    private final static int DELAY_PERIOD = 30;

    @Autowired
    public CreditDetailServiceImpl(CreditDetailRepository creditDetailRepository,
                                   CreditRepository creditRepository,
                                   WalletRepository walletRepository,
                                   UserRepository userRepository) {

        this.creditDetailRepository = creditDetailRepository;
        this.creditRepository = creditRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(CreditOpenRequestDTO openDTO, User user) {

        if (creditDetailRepository.findByUser(user)
                .filter(credit -> credit.getCreditStatus().equals(Status.OPEN))
                .isPresent()) {
            throw new ExistException(CREDIT_DETAIL_EXIST_MESSAGE);
        }

        Credit credit = creditRepository.findCreditByName(openDTO.getCreditName())
                .orElseThrow(() -> new NotFoundException(CREDIT_NOT_FOUND_MESSAGE));

        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(credit.getCurrencyType(), user)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

        wallet.setBalance(wallet.getBalance().add(openDTO.getMoney()));

        CreditDetail creditDetail = CreditDetailFactory.createInstance(credit, openDTO.getMoney());
        creditDetail.setUser(user);
        CreditDetail save = creditDetailRepository.save(creditDetail);
        log.info("CreditDetail was created: " + save);
    }

    @Override
    public CreditDetail findByUser(User user) {
        return creditDetailRepository.findByUser(user).orElseThrow(() -> new NotFoundException(CREDIT_DETAIL_NOT_FOUND_MESSAGE));
    }

    @Override
    public CreditDetail repayDebt(User user, CreditOpenRequestDTO openDTO) throws LackOfMoneyException {

        CreditDetail creditDetail = creditDetailRepository.findByUser(user)
                .filter(credit -> credit.getCreditStatus().equals(Status.OPEN))
                .orElseThrow(() -> new NotFoundException(CREDIT_DETAIL_NOT_FOUND_MESSAGE));

        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(creditDetail.getCredit().getCurrencyType(), user)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));

        if (wallet.getBalance().compareTo(openDTO.getMoney()) < 0) {
            throw new LackOfMoneyException();

        } else if (creditDetail.getCurrentDebt().subtract(openDTO.getMoney()).compareTo(BigDecimal.ZERO) <= 0) {

            wallet.setBalance(wallet.getBalance().subtract(creditDetail.getCurrentDebt()));

            creditDetail.setCurrentDebt(BigDecimal.ZERO);
            creditDetail.setCreditStatus(Status.CLOSED);
            LocalDateTime today = LocalDateTime.now();
            creditDetail.setLastModified(today);
            creditDetail.setFinishDate(today);
            log.info("CreditDetail was closed: " + creditDetail);

        } else {
            wallet.setBalance(wallet.getBalance().subtract(openDTO.getMoney()));
            creditDetail.setCurrentDebt(creditDetail.getCurrentDebt().subtract(openDTO.getMoney()));
            creditDetail.setLastModified(LocalDateTime.now());
            log.info("CreditDetail was changed: " + creditDetail);
        }

        return creditDetail;
    }

    @Override
    public CreditDetail findByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        return creditDetailRepository.findByUser(user).orElseThrow(() -> new NotFoundException(CREDIT_DETAIL_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<CreditDetail> findAllByStatus(Status status) {
        return creditDetailRepository.findAllByCreditStatus(status);
    }

    @Override
    public List<CreditDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate) {
        return creditDetailRepository.findAllByCreateDate(firstDate, secondDate, Sort.by("startDate"));
    }

    @Override
    public void deleteById(long creditId) throws UnremovableStatusException {
        CreditDetail creditDetail = creditDetailRepository.findById(creditId)
                .orElseThrow(() -> new NotFoundException(CREDIT_DETAIL_NOT_FOUND_MESSAGE));

        if (creditDetail.getCreditStatus().equals(Status.CLOSED)) {
            creditDetailRepository.delete(creditDetail);
            log.info("DepositDetail was deleted: " + creditDetail);
        } else {
            throw new UnremovableStatusException();
        }

    }

    @Override
    public List<CreditDetail> deleteAll() {
        List<CreditDetail> creditDetails = creditDetailRepository.deleteAllByCreditStatus(Status.CLOSED);
        creditDetails.forEach(creditDetail -> log.info("CreditDetail was deleted: " + creditDetail));
        return creditDetails;
    }

    @Override
    public void refreshAll() {

        List<CreditDetail> creditDetails = creditDetailRepository.findAll()
                .stream()
                .filter(creditDetail -> creditDetail.getCreditStatus().equals(Status.OPEN))
                .collect(Collectors.toList());

        LocalDateTime today = LocalDateTime.now();

        for (CreditDetail creditDetail : creditDetails) {

            LocalDateTime delay = creditDetail.getLastModified().plusDays(DELAY_PERIOD);

            if (delay.isBefore(today) || creditDetail.getFinishDate().isBefore(today)) {
                userRepository.findUserByCreditDetails(creditDetail).ifPresent(user -> {
                    user.setStatus(Status.BLOCKED);
                    log.info("User was blocked: " + user);
                });
            }

        }

    }

}
