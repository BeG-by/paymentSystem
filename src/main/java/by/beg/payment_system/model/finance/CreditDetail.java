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
public class CreditDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date finishDate;

    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @Column(precision = 20, scale = 2)
    private BigDecimal startDebt;

    @Column(precision = 20, scale = 2)
    private BigDecimal currentDebt;

    @Column(precision = 20, scale = 2)
    private BigDecimal fullDebt;

    @Enumerated(EnumType.STRING)
    private Status creditStatus = Status.OPEN;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Credit credit;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private User user;

    public CreditDetail(Date startDate, Date finishDate, Date lastUpdate, BigDecimal startDebt, BigDecimal currentDebt, BigDecimal fullDebt, Credit credit) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.lastUpdate = lastUpdate;
        this.startDebt = startDebt;
        this.currentDebt = currentDebt;
        this.fullDebt = fullDebt;
        this.credit = credit;
    }

}
