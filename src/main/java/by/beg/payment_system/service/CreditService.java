package by.beg.payment_system.service;

import by.beg.payment_system.model.finance.Credit;

import java.util.List;

public interface CreditService {

    void create(Credit credit);

    List<Credit> findAllAvailable();

    Credit findById(long creditId);

    List<Credit> findAll();

    void update(Credit credit);

    void delete(long creditId);

    List<Credit> deleteAll();

}
