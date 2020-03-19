package by.beg.payment_system.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "\\w+[@]\\w+\\.(com|by|ru)", message = "Incorrect email")
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

    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private Address address;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;


    public enum UserRole {
        USER, ADMIN
    }

}
