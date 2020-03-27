package by.beg.payment_system.service;

import by.beg.payment_system.exception.DepositNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.finance.Deposit;

import java.util.List;

public interface DepositService {

    Deposit create(Deposit deposit);

    List<Deposit> getAllAvailable();

    Deposit update(Deposit deposit);

    Deposit findById(long depositId) throws DepositNotFoundException;

    List<Deposit> getAll();

    Deposit delete(long depositId) throws DepositNotFoundException, UnremovableStatusException;

    List<Deposit> deleteAll();

}
