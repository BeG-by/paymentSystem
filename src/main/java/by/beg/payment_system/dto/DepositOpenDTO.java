package by.beg.payment_system.dto;

import by.beg.payment_system.model.finance.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.enumerations.DepositType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositOpenDTO {

    @DecimalMin(message = "Value \"money\" must be more than 1", value = "1")
    private BigDecimal money;

    @NotNull(message = "Deposit type can't be empty.")
    private DepositType depositType;

    @NotNull(message = "Currency type can't be empty.")
    private CurrencyType currencyType;

}