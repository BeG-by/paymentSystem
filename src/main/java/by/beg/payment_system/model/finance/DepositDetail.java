package by.beg.payment_system.model.finance;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date finishDate;

    @Column(precision = 20, scale = 2)
    private BigDecimal balance;

    @Column(precision = 20, scale = 2)
    private BigDecimal returnBalance;

    @Enumerated(EnumType.STRING)
    private Status depositDetailStatus = Status.UNAVAILABLE;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Deposit deposit;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private User user;


    public DepositDetail(Date startDate, Date finishDate, BigDecimal balance, BigDecimal returnBalance, Deposit deposit) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.balance = balance;
        this.returnBalance = returnBalance;
        this.deposit = deposit;
    }
}
