package by.beg.payment_system.service.impl;

import by.beg.payment_system.exception.CreditIsPresentException;
import by.beg.payment_system.exception.CreditNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.repository.CreditRepository;
import by.beg.payment_system.service.CreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CreditServiceImpl implements CreditService {

    private CreditRepository creditRepository;

    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Override
    public void create(Credit credit) throws CreditIsPresentException {

        if (creditRepository.findCreditByName(credit.getName()).isPresent()) {
            throw new CreditIsPresentException();
        }

        Credit save = creditRepository.save(credit);
        log.info("Credit was created: " + save);

    }

    @Override
    public List<Credit> findAllAvailable() {
        return creditRepository.findAll()
                .stream()
                .filter(credit -> credit.getStatus().equals(Status.OPEN))
                .collect(Collectors.toList());
    }


    @Override
    public Credit findById(long creditId) throws CreditNotFoundException {
        return creditRepository.findById(creditId).orElseThrow(CreditNotFoundException::new);
    }

    @Override
    public List<Credit> findAll() {
        return creditRepository.findAll();
    }

    @Override
    public void update(Credit credit) throws CreditNotFoundException {
        creditRepository.findById(credit.getId()).orElseThrow(CreditNotFoundException::new);
        Credit save = creditRepository.save(credit);
        log.info("Credit was updated: " + save);
    }

    @Override
    public void delete(long creditId) throws CreditNotFoundException, UnremovableStatusException {
        Credit credit = creditRepository.findById(creditId).orElseThrow(CreditNotFoundException::new);

        if (credit.getStatus().equals(Status.CLOSED)) {
            creditRepository.delete(credit);
            log.info("Credit was deleted: " + credit);
        } else {
            throw new UnremovableStatusException();
        }

    }

    @Override
    public List<Credit> deleteAll() {
        List<Credit> credits = creditRepository.deleteAllByStatus(Status.CLOSED);
        credits.forEach(credit -> log.info("Credit was deleted: " + credit));
        return credits;
    }

}
