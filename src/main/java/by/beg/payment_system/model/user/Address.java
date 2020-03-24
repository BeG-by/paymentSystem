package by.beg.payment_system.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "City can't be empty")
    private String city;

    @NotBlank(message = "Street can't be empty")
    private String street;

    @NotBlank(message = "House can't be empty")
    private String house;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

}
