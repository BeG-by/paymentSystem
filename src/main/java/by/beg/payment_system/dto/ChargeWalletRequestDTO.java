package by.beg.payment_system.dto;

import by.beg.payment_system.model.enumerations.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeWalletRequestDTO {

    @DecimalMin(message = "Value \"money\" must be more than 1", value = "1")
    private BigDecimal money;

    @NotNull(message = "Type can't be empty.")
    private CurrencyType type;

}

