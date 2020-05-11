package by.beg.payment_system.model.finance;

import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String walletValue;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creatDate = LocalDateTime.now();

    @Column(scale = 2, precision = 20)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private User user;

    public Wallet(String walletValue, CurrencyType type, User user) {
        this.walletValue = walletValue;
        this.currencyType = type;
        this.user = user;
    }


}
