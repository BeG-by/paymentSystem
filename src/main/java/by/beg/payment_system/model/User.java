package by.beg.payment_system.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
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

    @Size(min = 4, max = 16, message = "Password must have 4-16 symbols")
    private String password;

    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @NotBlank(message = "Passport number can't be empty")
    private String passport;

    @NotNull(message = "Date can't be empty")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserRole userRole = UserRole.USER;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date lastUpdate = new Date();

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
    private List<Deposit> deposits;


    public enum UserRole {
        USER, ADMIN
    }

}
