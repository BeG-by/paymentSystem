package by.beg.payment_system.repository;

import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.enumerations.DepositStatus;
import by.beg.payment_system.model.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findAllByUser(User user);

    List<Deposit> findAllByDepositStatus(DepositStatus depositStatus);

    @Query(value = "FROM Deposit d WHERE d.startDate > :firstDate AND d.startDate < :secondDate")
    List<Deposit> filterByCreateDate(@Param("firstDate") Date firstDate, @Param("secondDate") Date secondDate, Sort sort);
}
