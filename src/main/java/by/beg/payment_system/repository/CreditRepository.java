package by.beg.payment_system.repository;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    List<Credit> deleteAllByStatus(Status status);

    Optional<Credit> findCreditByName(String name);

}
