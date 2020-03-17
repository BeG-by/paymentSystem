package by.beg.payment_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
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

}
