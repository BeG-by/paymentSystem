package by.beg.payment_system.model.finance;

import by.beg.payment_system.model.finance.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.enumerations.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency can't be null")
    private CurrencyType currencyType;

    @Positive(message = "Period must be more than 1")
    private int period;

    @DecimalMin(message = "Value \"money\" must be more than 0", value = "0")
    private BigDecimal rate;

    @NotNull(message = "Capitalization can't be null")
    private boolean isCapitalization;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    @OneToMany(cascade = CascadeType.PERSIST , mappedBy = "deposit")
    @JsonIgnore
    @ToString.Exclude
    private List<DepositDetail> depositDetails;


}
