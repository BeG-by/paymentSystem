package by.beg.payment_system.repository;


import by.beg.payment_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {


}
