package by.beg.payment_system.model.finance;

import by.beg.payment_system.model.finance.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.enumerations.DepositStatus;
import by.beg.payment_system.model.finance.enumerations.DepositType;
import by.beg.payment_system.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private DepositType depositType;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    private int days;

    @Temporal(TemporalType.DATE)
    private Date finishDate;

    @Column(precision = 20, scale = 2)
    private BigDecimal balance;

    @Column(precision = 20, scale = 2)
    private BigDecimal returnBalance;

    private double rate;

    private boolean isCapitalization;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus = DepositStatus.UNAVAILABLE;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private User user;


    public Deposit(DepositType depositType, CurrencyType currencyType, Date startDate, int days, Date finishDate,
                   double rate, boolean isCapitalization) {
        this.depositType = depositType;
        this.currencyType = currencyType;
        this.startDate = startDate;
        this.days = days;
        this.finishDate = finishDate;
        this.rate = rate;
        this.isCapitalization = isCapitalization;

    }
}
