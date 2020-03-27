package by.beg.payment_system.repository;

import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.enumerations.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> deleteAllByStatus(Status status);

}
