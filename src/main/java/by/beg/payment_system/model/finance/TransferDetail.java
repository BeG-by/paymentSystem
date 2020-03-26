package by.beg.payment_system.model.finance;

import by.beg.payment_system.model.finance.enumerations.CurrencyType;
import by.beg.payment_system.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @Valid
    private User user;

    @NotNull(message = "Incorrect wallet type.")
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Pattern(regexp = "[A-Z]{3}\\d{9}", message = "Wallet value isn't valid")
    private String targetWalletValue;

    @DecimalMin(message = "Value \"money\" must be more than 1", value = "1")
    @Column(scale = 2, precision = 20)
    private BigDecimal moneySend;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(scale = 2 , precision = 20)
    private BigDecimal moneyReceive;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date date = new Date();

}
