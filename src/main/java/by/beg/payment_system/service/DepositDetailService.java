package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.user.User;

import java.util.Date;
import java.util.List;

public interface DepositDetailService {

    DepositDetail create(DepositOpenDTO openDTO, User user) throws WalletNotFoundException, LackOfMoneyException, DepositNotFoundException, CurrencyConverterException;

    List<DepositDetail> getAllByUser(User user);

    List<DepositDetail> pickUp(User user) throws WalletNotFoundException;

    List<DepositDetail> getAllForAdmin(long userId) throws UserNotFoundException;

    List<DepositDetail> getAllForAdminByStatus(Status status);

    List<DepositDetail> filterByCreateDate(Date firstDate, Date secondDate);

    DepositDetail delete(long deleteId) throws DepositNotFoundException, UnremovableStatusException;

    List<DepositDetail> deleteAll();

    void refreshAll();


}
