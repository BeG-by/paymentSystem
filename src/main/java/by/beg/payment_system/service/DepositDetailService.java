package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.DepositNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.enumerations.Status;
import by.beg.payment_system.model.user.User;

import java.util.Date;
import java.util.List;

public interface DepositDetailService {

    DepositDetail create(DepositOpenDTO openDTO, User user) throws WalletNotFoundException, LackOfMoneyException, DepositNotFoundException;

    List<DepositDetail> getAllByUser(User user);

    List<DepositDetail> pickUp(User user) throws WalletNotFoundException;

    List<DepositDetail> getAllForAdmin(long userId);

    List<DepositDetail> getAllForAdminByStatus(Status status);

    List<DepositDetail> filterByCreateDate(Date firstDate, Date secondDate);

    DepositDetail delete(long deleteId) throws DepositNotFoundException, UnremovableStatusException;

    List<DepositDetail> deleteAll();

    List<DepositDetail> refreshAll();


}
