package by.beg.payment_system.service;

import by.beg.payment_system.exception.CreditIsPresentException;
import by.beg.payment_system.exception.CreditNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.finance.Credit;

import java.util.List;

public interface CreditService {

    Credit create(Credit credit) throws CreditIsPresentException;

    List<Credit> getAllAvailable();

    Credit findById(long creditId) throws CreditNotFoundException;

    List<Credit> getAll();

    Credit update(Credit credit) throws CreditNotFoundException;

    Credit delete(long creditId) throws CreditNotFoundException, UnremovableStatusException;

    List<Credit> deleteAll();

}
