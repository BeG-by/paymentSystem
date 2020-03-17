package by.beg.payment_system.model;

import by.beg.payment_system.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//validation ?
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private WalletType walletType;

    @Temporal(TemporalType.TIMESTAMP) // pattern - ?
    private Date creatingDate;

    private double balance;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;


    enum WalletType {
        BYN, USD, EURO
    }

}
