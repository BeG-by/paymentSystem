package by.beg.payment_system.service;

import by.beg.payment_system.dto.CreditOpenDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.user.User;

import java.util.Date;
import java.util.List;

public interface CreditDetailService {

    CreditDetail create(CreditOpenDTO openDTO, User user) throws WalletNotFoundException, CreditDetailIsPresentException, CreditNotFoundException;

    CreditDetail getByUser(User user) throws CreditNotFoundException;

    CreditDetail repayDebt(User user, CreditOpenDTO openDTO) throws CreditNotFoundException, WalletNotFoundException, LackOfMoneyException;

    CreditDetail getForAdmin(long userId) throws UserNotFoundException, CreditNotFoundException;

    List<CreditDetail> getAllForAdminByStatus(Status status);

    List<CreditDetail> filterByCreateDate(Date firstDate, Date secondDate);

    CreditDetail delete(long creditId) throws CreditNotFoundException, UnremovableStatusException;

    List<CreditDetail> deleteAll();

    void refreshAll();


}
