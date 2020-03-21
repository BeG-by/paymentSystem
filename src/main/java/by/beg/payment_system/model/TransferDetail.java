package by.beg.payment_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @Pattern(regexp = "[A-Z]{3}\\d{9}" , message = "Wallet value isn't valid")
    private String targetWalletValue;

    @Min(message = "Value \"money\" must be more than 1", value = 1)
    private double moneySend;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double moneyReceive;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date date = new Date();

}