package by.beg.payment_system.service;

import by.beg.payment_system.dto.request.CreditOpenRequestDTO;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CreditDetailService {

    void create(CreditOpenRequestDTO openDTO, User user);

    CreditDetail findByUser(User user);

    CreditDetail repayDebt(User user, CreditOpenRequestDTO openDTO) throws LackOfMoneyException;

    CreditDetail findByUserId(long userId);

    List<CreditDetail> findAllByStatus(Status status);

    List<CreditDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

    void deleteById(long creditId);

    List<CreditDetail> deleteAll();

    void refreshAll();

}
