package by.beg.payment_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditOpenRequestDTO {

    @DecimalMin(message = "Value \"money\" must be more than 1", value = "1")
    private BigDecimal money;

    @Pattern(regexp = "[A-Z]{3}\\d+", message = "Wrong credit name")
    private String creditName;

}
