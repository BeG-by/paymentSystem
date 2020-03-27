package by.beg.payment_system.repository;

import by.beg.payment_system.model.finance.TransferDetail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransferRepository extends JpaRepository<TransferDetail, Long> {

    @Query(value = "FROM TransferDetail d WHERE d.date > :firstDate AND d.date < :secondDate")
    List<TransferDetail> filterByDate(@Param("firstDate") Date firstDate, @Param("secondDate") Date secondDate, Sort sort);

}
