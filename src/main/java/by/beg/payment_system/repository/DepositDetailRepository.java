package by.beg.payment_system.repository;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DepositDetailRepository extends JpaRepository<DepositDetail, Long> {

    List<DepositDetail> findAllByUser(User user);

    List<DepositDetail> findAllByDepositDetailStatus(Status status);

    @Query(value = "FROM DepositDetail d WHERE d.startDate > :firstDate AND d.startDate < :secondDate")
    List<DepositDetail> filterByCreateDate(@Param("firstDate") Date firstDate, @Param("secondDate") Date secondDate, Sort sort);

    List<DepositDetail> deleteAllByDepositDetailStatus(Status status);
}
