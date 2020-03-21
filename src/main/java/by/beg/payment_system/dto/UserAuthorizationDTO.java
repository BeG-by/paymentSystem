package by.beg.payment_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorizationDTO {

    @Pattern(regexp = "\\w+[@]\\w+\\.\\w+", message = "Incorrect email")
    private String email;

    @Size(min = 4, max = 16, message = "Password must have 4-16 symbols")
    private String password;

}
