package by.beg.payment_system.repository;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CreditDetailRepository extends JpaRepository<CreditDetail, Long> {

    Optional<CreditDetail> findByUser(User user);

    List<CreditDetail> findAllByCreditStatus(Status status);

    @Query(value = "FROM CreditDetail c WHERE c.startDate > :firstDate AND c.startDate < :secondDate")
    List<CreditDetail> findAllByCreateDate(@Param("firstDate") LocalDateTime firstDate, @Param("secondDate") LocalDateTime secondDate, Sort sort);

    List<CreditDetail> deleteAllByCreditStatus(Status status);

}
