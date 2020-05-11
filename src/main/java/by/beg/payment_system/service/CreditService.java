package by.beg.payment_system.service;

import by.beg.payment_system.exception.CreditIsPresentException;
import by.beg.payment_system.exception.CreditNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.finance.Credit;

import java.util.List;

public interface CreditService {

    void create(Credit credit) throws CreditIsPresentException;

    List<Credit> findAllAvailable();

    Credit findById(long creditId) throws CreditNotFoundException;

    List<Credit> findAll();

    void update(Credit credit) throws CreditNotFoundException;

    void delete(long creditId) throws CreditNotFoundException, UnremovableStatusException;

    List<Credit> deleteAll();

}
