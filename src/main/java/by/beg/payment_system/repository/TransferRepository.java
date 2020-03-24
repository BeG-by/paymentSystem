package by.beg.payment_system.repository;

import by.beg.payment_system.model.finance.TransferDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<TransferDetail, Long> {
}
