package by.beg.payment_system.service;

import by.beg.payment_system.model.finance.Deposit;

import java.util.List;

public interface DepositService {

    void create(Deposit deposit);

    List<Deposit> findAllAvailable();

    Deposit findById(long depositId);

    List<Deposit> findAll();

    void update(Deposit deposit);

    void delete(long depositId);

    List<Deposit> deleteAll();

}
