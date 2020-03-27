package by.beg.payment_system.service;

import by.beg.payment_system.exception.DepositNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.enumerations.Status;
import by.beg.payment_system.repository.DepositRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DepositServiceImpl implements DepositService {

    private DepositRepository depositRepository;

    public DepositServiceImpl(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Override
    public Deposit create(Deposit deposit) {
        Deposit save = depositRepository.save(deposit);
        log.info("Deposit was created: " + save);
        return save;
    }

    @Override
    public List<Deposit> getAllAvailable() {
        return depositRepository.findAll().stream()
                .filter(deposit -> deposit.getStatus().equals(Status.AVAILABLE)).collect(Collectors.toList());
    }

    @Override
    public Deposit update(Deposit deposit) {
        Deposit save = depositRepository.save(deposit);
        log.info("Deposit was updated: " + save);
        return save;
    }

    @Override
    public Deposit findById(long depositId) throws DepositNotFoundException {
        return depositRepository.findById(depositId).orElseThrow(DepositNotFoundException::new);
    }

    @Override
    public List<Deposit> getAll() {
        return depositRepository.findAll();
    }

    @Override
    public Deposit delete(long depositId) throws DepositNotFoundException, UnremovableStatusException {
        Deposit byId = depositRepository.findById(depositId).orElseThrow(DepositNotFoundException::new);

        if (byId.getStatus().equals(Status.DELETED)) {
            depositRepository.delete(byId);
            log.info("Deposit was deleted: " + byId);
        } else {
            throw new UnremovableStatusException();
        }

        return byId;
    }

    @Override
    public List<Deposit> deleteAll() {

        List<Deposit> deposits = depositRepository.deleteAllByStatus(Status.DELETED);

        for (Deposit currentDeposit : deposits) {
            log.info("Deposit was deleted: " + currentDeposit);
        }

        return deposits;
    }
}
