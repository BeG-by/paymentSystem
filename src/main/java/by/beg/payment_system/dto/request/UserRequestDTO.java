//package by.beg.payment_system.dto.request;
//
//import by.beg.payment_system.model.user.Address;
//import by.beg.payment_system.model.user.User;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class UserRequestDTO {
//
//    @Pattern(regexp = "\\w+[@]\\w+\\.\\w+", message = "Incorrect email")
//    private String email;
//
//    @Size(min = 4, max = 16, message = "Password must have 4-16 symbols")
//    private String password;
//
//    @NotBlank(message = "First name can't be empty")
//    private String firstName;
//
//    @NotBlank(message = "Last name can't be empty")
//    private String lastName;
//
//    @NotBlank(message = "Passport number can't be empty")
//    private String passport;
//
//    @NotNull(message = "Date can't be empty")
//    @JsonFormat(pattern = "dd-MM-yyyy")
//    private LocalDate birthday;
//
//    @Valid
//    private Address address;
//
//
//    public User toUser() {
//        User user = new User();
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setPassport(passport);
//        user.setBirthday(birthday);
//        user.setAddress(address);
//        user.setWallets(new HashSet<>());
//        user.setTransferDetails(new ArrayList<>());
//        user.setDepositDetails(new ArrayList<>());
//        user.setCreditDetails(new ArrayList<>());
//        return user;
//    }
//
//
//}
