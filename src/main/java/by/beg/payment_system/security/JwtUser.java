package by.beg.payment_system.security;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JwtUser implements UserDetails {

    private long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String passport;
    private LocalDate birthday;
    private Status status;
    private LocalDateTime lastUpdate;
    private Address address;
    private Set<Wallet> wallets;
    private List<TransferDetail> transferDetails;
    private List<DepositDetail> depositDetails;
    private List<CreditDetail> creditDetails;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnable;

    public JwtUser(long id,
                   String email,
                   String password,
                   String firstName,
                   String lastName, String passport,
                   LocalDate birthday, Status status,
                   LocalDateTime lastUpdate,
                   Address address,
                   Set<Wallet> wallets,
                   List<TransferDetail> transferDetails,
                   List<DepositDetail> depositDetails,
                   List<CreditDetail> creditDetails,
                   Collection<? extends GrantedAuthority> authorities,
                   boolean isEnable) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
        this.birthday = birthday;
        this.status = status;
        this.lastUpdate = lastUpdate;
        this.address = address;
        this.wallets = wallets;
        this.transferDetails = transferDetails;
        this.depositDetails = depositDetails;
        this.creditDetails = creditDetails;
        this.authorities = authorities;
        this.isEnable = isEnable;
    }


    public long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassport() {
        return passport;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }


    public List<TransferDetail> getTransferDetails() {
        return transferDetails;
    }


    public List<DepositDetail> getDepositDetails() {
        return depositDetails;
    }


    public List<CreditDetail> getCreditDetails() {
        return creditDetails;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
