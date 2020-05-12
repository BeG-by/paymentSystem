package by.beg.payment_system.service.impl;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static by.beg.payment_system.service.util.MessageConstant.*;

@Service
@Transactional
@Slf4j
public class DepositServiceImpl implements DepositService {

    private DepositRepository depositRepository;


    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Override
    public void create(Deposit deposit) {

        if (depositRepository.findByName(deposit.getName()).isPresent()) {
            throw new ExistException(DEPOSIT_EXIST_MESSAGE);
        }

        Deposit save = depositRepository.save(deposit);
        log.info("Deposit was created: " + save);

    }

    @Override
    public List<Deposit> findAllAvailable() {
        return depositRepository.findAll()
                .stream()
                .filter(deposit -> deposit.getStatus().equals(Status.OPEN))
                .collect(Collectors.toList());
    }

    @Override
    public Deposit findById(long depositId) {
        return depositRepository.findById(depositId).orElseThrow(() -> new NotFoundException(DEPOSIT_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<Deposit> findAll() {
        return depositRepository.findAll();
    }

    @Override
    public void update(Deposit deposit) {
        depositRepository.findById(deposit.getId()).orElseThrow(() -> new NotFoundException(DEPOSIT_NOT_FOUND_MESSAGE));
        Deposit save = depositRepository.save(deposit);
        log.info("Deposit was updated: " + save);
    }

    @Override
    public void delete(long depositId) {
        Deposit deposit = depositRepository.findById(depositId).orElseThrow(() -> new NotFoundException(DEPOSIT_NOT_FOUND_MESSAGE));

        if (deposit.getStatus().equals(Status.CLOSED)) {
            depositRepository.delete(deposit);
            log.info("Deposit was deleted: " + deposit);
        } else {
            throw new UnremovableStatusException();
        }

    }

    @Override
    public List<Deposit> deleteAll() {
        List<Deposit> deposits = depositRepository.deleteAllByStatus(Status.CLOSED);
        deposits.forEach(deposit -> log.info("Deposit was deleted: " + deposit));
        return deposits;
    }

}
