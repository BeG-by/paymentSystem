package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenRequestDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface DepositDetailService {

    void create(DepositOpenRequestDTO openDTO, User user) throws WalletNotFoundException, LackOfMoneyException, DepositNotFoundException, CurrencyConverterException;

    List<DepositDetail> findAllByUser(User user);

    List<DepositDetail> pickUp(User user) throws WalletNotFoundException;

    List<DepositDetail> findAllById(long userId) throws UserNotFoundException;

    List<DepositDetail> findAllByStatus(Status status);

    List<DepositDetail> findAllBetweenDate(LocalDateTime firstDate, LocalDateTime secondDate);

    void deleteById(long deleteId) throws DepositNotFoundException, UnremovableStatusException;

    List<DepositDetail> deleteAll();

    void refreshAll();

}
