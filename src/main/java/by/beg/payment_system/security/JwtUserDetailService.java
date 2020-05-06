package by.beg.payment_system.security;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JwtUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public JwtUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User with email (" + email + ") not found"));

        JwtUser jwtUser = new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassport(),
                user.getBirthday(),
                user.getStatus(),
                user.getLastUpdate(),
                user.getAddress(),
                user.getWallets(),
                user.getTransferDetails(),
                user.getDepositDetails(),
                user.getCreditDetails(),
                createGrantedAuthority(user.getUserRole()),
                user.getStatus().equals(Status.ACTIVE)
        );

        log.info("User with email (" + email + ") was loaded");
        return jwtUser;
    }

    private List<GrantedAuthority> createGrantedAuthority(UserRole userRole) {
        return userRole.equals(UserRole.ADMIN) ?
                List.of(new SimpleGrantedAuthority(UserRole.ADMIN.toString()), new SimpleGrantedAuthority(UserRole.USER.toString())) :
                List.of(new SimpleGrantedAuthority(userRole.toString()));
    }

}
