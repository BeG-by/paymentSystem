package by.beg.payment_system.service;

import by.beg.payment_system.dto.request.DepositOpenRequestDTO;
import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface DepositDetailService {

    void create(DepositOpenRequestDTO openDTO, User user) throws LackOfMoneyException, CurrencyConverterException;

    List<DepositDetail> findAllByUser(User user);

    List<DepositDetail> pickUp(User user);

    List<DepositDetail> findAllById(long userId);

    List<DepositDetail> findAllByStatus(Status status);

    List<DepositDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

    void deleteById(long deleteId) throws UnremovableStatusException;

    List<DepositDetail> deleteAll();

    void refreshAll();

}
