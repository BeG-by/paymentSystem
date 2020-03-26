package by.beg.payment_system.service;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.enumerations.DepositStatus;
import by.beg.payment_system.model.finance.enumerations.DepositType;
import by.beg.payment_system.model.user.User;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

public interface DepositService {

    Deposit create(DepositOpenDTO openDTO, User user) throws WalletNotFoundException, LackOfMoneyException;

    List<Deposit> getDepositsDescription();

    List<Deposit> getAllByUser(User user);

    List<Deposit> pickUp(User user) throws WalletNotFoundException;

    List<Deposit> getAllForAdmin(long userId);

    List<Deposit> getAllForAdminByStatus(DepositStatus depositStatus);

    List<Deposit> filterByCreateDate(Date firstDate, Date secondDate);


}
