package by.beg.payment_system.model.finance;

import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.enumerations.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "[A-Z]{3}\\d+", message = "Wrong credit name")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency can't be null")
    private CurrencyType currencyType;

    @Positive(message = "Period must be more than 1")
    private int period;

    @DecimalMin(message = "Value \"money\" must be more than 0", value = "0")
    private BigDecimal rate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "credit")
    @JsonIgnore
    @ToString.Exclude
    private List<CreditDetail> creditDetails;


}
