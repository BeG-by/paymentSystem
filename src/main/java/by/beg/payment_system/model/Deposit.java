package by.beg.payment_system.model;

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

    private double rate;

    private boolean isCapitalization;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private User user;


    public Deposit(DepositType depositType, CurrencyType currencyType, Date startDate, int days, Date finishDate, double rate, boolean isCapitalization) {
        this.depositType = depositType;
        this.currencyType = currencyType;
        this.startDate = startDate;
        this.days = days;
        this.finishDate = finishDate;
        this.rate = rate;
        this.isCapitalization = isCapitalization;
    }
}
