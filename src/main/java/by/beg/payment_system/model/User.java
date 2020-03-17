package by.beg.payment_system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "\\w+[@]\\w+\\.(com|by|ru)", message = "Incorrect email")
    private String email;

    @Length(min = 4, max = 16, message = "Password must have 4-16 symbols")
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

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;


}
