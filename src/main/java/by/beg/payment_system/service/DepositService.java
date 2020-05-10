package by.beg.payment_system.service;

import by.beg.payment_system.exception.DepositIsPresentException;
import by.beg.payment_system.exception.DepositNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.finance.Deposit;

import java.util.List;

public interface DepositService {

    void create(Deposit deposit) throws DepositIsPresentException;

    List<Deposit> findAllAvailable();

    Deposit findById(long depositId) throws DepositNotFoundException;

    List<Deposit> findAll();

    void update(Deposit deposit) throws DepositNotFoundException;

    void delete(long depositId) throws DepositNotFoundException, UnremovableStatusException;

    List<Deposit> deleteAll();

}
