package by.beg.payment_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {

     private String email;
     private String token;

}
