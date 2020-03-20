package by.beg.payment_system.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    private WalletType walletType;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Date creatDate = new Date();

    private double balance = 0;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    public Wallet(String walletValue, WalletType type, User user) {
        this.walletValue = walletValue;
        this.walletType = type;
        this.user = user;
    }


    public enum WalletType {
        BYN, USD, EUR, RUB
    }

}
