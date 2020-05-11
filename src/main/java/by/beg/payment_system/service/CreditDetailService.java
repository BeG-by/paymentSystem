package by.beg.payment_system.service;

import by.beg.payment_system.dto.CreditOpenRequestDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CreditDetailService {

    void create(CreditOpenRequestDTO openDTO, User user) throws WalletNotFoundException, CreditDetailIsPresentException, CreditNotFoundException;

    CreditDetail findByUser(User user) throws CreditNotFoundException;

    CreditDetail repayDebt(User user, CreditOpenRequestDTO openDTO) throws CreditNotFoundException, WalletNotFoundException, LackOfMoneyException;

    CreditDetail findByUserId(long userId) throws UserNotFoundException, CreditNotFoundException;

    List<CreditDetail> findAllByStatus(Status status);

    List<CreditDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

    void deleteById(long creditId) throws CreditNotFoundException, UnremovableStatusException;

    List<CreditDetail> deleteAll();

    void refreshAll();

}
