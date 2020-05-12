package by.beg.payment_system.dto.response;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.Address;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private UserRole userRole;
    private Status status;
    private LocalDateTime lastModified;
    private Address address;
    private Set<Wallet> wallets;
    private List<DepositDetail> depositDetails;
    private List<CreditDetail> creditDetails;

    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthday(),
                user.getUserRole(),
                user.getStatus(),
                user.getLastModified(),
                user.getAddress(),
                user.getWallets(),
                user.getDepositDetails(),
                user.getCreditDetails());
    }
}
