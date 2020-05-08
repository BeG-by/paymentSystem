package by.beg.payment_system.model.user;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.security.Token;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "\\w+[@]\\w+\\.\\w+", message = "Incorrect email")
    private String email;

//    @Size(min = 4, max = 16, message = "Password must have 4-16 symbols")
    private String password;

    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @NotBlank(message = "Passport number can't be empty")
    private String passport;

    @NotNull(message = "Date can't be empty")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserRole userRole = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Status status = Status.ACTIVE;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Valid
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonManagedReference
    private Set<Wallet> wallets;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private List<TransferDetail> transferDetails;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonManagedReference
    private List<DepositDetail> depositDetails;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonManagedReference
    private List<CreditDetail> creditDetails;


}
