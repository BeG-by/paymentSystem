package by.beg.payment_system.dto;

import by.beg.payment_system.model.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeWalletDTO {

    @Min(message = "Value \"money\" must be more than 1", value = 1)
    private double money;

    @NotNull(message = "Type can't be empty.")
    private CurrencyType type;

}

