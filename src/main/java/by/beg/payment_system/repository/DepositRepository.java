package by.beg.payment_system.repository;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> deleteAllByStatus(Status status);

    Optional<Deposit> findByName(String name);

}
