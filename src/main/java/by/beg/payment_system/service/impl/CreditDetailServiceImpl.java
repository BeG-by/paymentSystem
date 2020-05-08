package by.beg.payment_system.service.impl;

import by.beg.payment_system.dto.CreditOpenDTO;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class CreditDetailServiceImpl implements CreditDetailService {

    private CreditDetailRepository creditDetailRepository;
    private CreditRepository creditRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;

    public CreditDetailServiceImpl(CreditDetailRepository creditDetailRepository, CreditRepository creditRepository,
                                   WalletRepository walletRepository, UserRepository userRepository) {
        this.creditDetailRepository = creditDetailRepository;
        this.creditRepository = creditRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CreditDetail create(CreditOpenDTO openDTO, User user)
            throws WalletNotFoundException, CreditDetailIsPresentException, CreditNotFoundException {

        if (creditDetailRepository.findByUser(user).isPresent()) {
            throw new CreditDetailIsPresentException();
        }

        Credit credit = creditRepository.findCreditByName(openDTO.getCreditName()).orElseThrow(CreditNotFoundException::new);
        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(credit.getCurrencyType(), user).orElseThrow(WalletNotFoundException::new);
        wallet.setBalance(wallet.getBalance().add(openDTO.getMoney()));

        CreditDetail instance = CreditDetailFactory.getInstance(credit, openDTO.getMoney());
        instance.setUser(user);
        CreditDetail save = creditDetailRepository.save(instance);
        log.info("CreditDetail was created: " + save);
        return save;
    }

    @Override
    public CreditDetail getByUser(User user) throws CreditNotFoundException {
        return creditDetailRepository.findByUser(user).orElseThrow(CreditNotFoundException::new);
    }

    @Override
    public CreditDetail repayDebt(User user, CreditOpenDTO openDTO)
            throws CreditNotFoundException, WalletNotFoundException, LackOfMoneyException {

        CreditDetail creditDetail = creditDetailRepository.findByUser(user).
                filter(credit -> credit.getCreditStatus().equals(Status.OPEN)).orElseThrow(CreditNotFoundException::new);

        Wallet wallet = walletRepository.findByCurrencyTypeAndUser(creditDetail.getCredit().getCurrencyType(), user).orElseThrow(WalletNotFoundException::new);

        if (wallet.getBalance().compareTo(openDTO.getMoney()) < 0) {
            throw new LackOfMoneyException();

        } else if (creditDetail.getCurrentDebt().subtract(openDTO.getMoney()).compareTo(new BigDecimal(0)) <= 0) {

            wallet.setBalance(wallet.getBalance().subtract(creditDetail.getCurrentDebt()));

            creditDetail.setCurrentDebt(new BigDecimal(0));
            creditDetail.setCreditStatus(Status.CLOSED);
            Date today = new Date();
            creditDetail.setLastUpdate(today);
            creditDetail.setFinishDate(today);
            log.info("CreditDetail was closed: " + creditDetail);

        } else {
            wallet.setBalance(wallet.getBalance().subtract(openDTO.getMoney()));
            creditDetail.setCurrentDebt(creditDetail.getCurrentDebt().subtract(openDTO.getMoney()));
            creditDetail.setLastUpdate(new Date());
            log.info("CreditDetail was changed: " + creditDetail);
        }

        return creditDetail;
    }

    @Override
    public CreditDetail getForAdmin(long userId) throws UserNotFoundException, CreditNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return creditDetailRepository.findByUser(user).orElseThrow(CreditNotFoundException::new);
    }

    @Override
    public List<CreditDetail> getAllForAdminByStatus(Status status) {
        return creditDetailRepository.findAllByCreditStatus(status);
    }

    @Override
    public List<CreditDetail> filterByCreateDate(Date firstDate, Date secondDate) {
        return creditDetailRepository.filterByCreateDate(firstDate, secondDate, Sort.by("startDate"));
    }

    @Override
    public CreditDetail delete(long creditId) throws CreditNotFoundException, UnremovableStatusException {
        CreditDetail creditDetail = creditDetailRepository.findById(creditId).orElseThrow(CreditNotFoundException::new);

        if (creditDetail.getCreditStatus().equals(Status.CLOSED)) {
            creditDetailRepository.delete(creditDetail);
            log.info("DepositDetail was deleted: " + creditDetail);
        } else {
            throw new UnremovableStatusException();
        }

        return creditDetail;
    }

    @Override
    public List<CreditDetail> deleteAll() {
        List<CreditDetail> creditDetails = creditDetailRepository.deleteAllByCreditStatus(Status.CLOSED);
        creditDetails.forEach(creditDetail -> log.info("CreditDetail was deleted: " + creditDetail));
        return creditDetails;
    }

    @Override
    public void refreshAll() {

        List<CreditDetail> creditDetails = creditDetailRepository.findAll().stream().
                filter(creditDetail -> creditDetail.getCreditStatus().equals(Status.OPEN)).collect(Collectors.toList());

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();

        for (CreditDetail creditDetail : creditDetails) {

            calendar.setTime(creditDetail.getLastUpdate());
            calendar.add(Calendar.DATE, 30);

            if (calendar.getTime().before(today) || creditDetail.getFinishDate().before(today)) {
                userRepository.findUserByCreditDetails(creditDetail).ifPresent(user -> {
                    user.setStatus(Status.BLOCKED);
                    log.info("User was blocked: " + user);
                });
            }

        }

    }

}
