package by.beg.payment_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date start;

    @Temporal(TemporalType.DATE)
    private Date end;

    private double balance;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;


}
