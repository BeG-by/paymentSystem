package by.beg.payment_system.security.jwt;

import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                createGrantedAuthority(user.getUserRole()),
                user.getStatus().equals(Status.ACTIVE)
        );

    }


    private List<GrantedAuthority> createGrantedAuthority(UserRole userRole) {
        return userRole.equals(UserRole.ADMIN) ?
                List.of(new SimpleGrantedAuthority(UserRole.ADMIN.toString()), new SimpleGrantedAuthority(UserRole.USER.toString())) :
                List.of(new SimpleGrantedAuthority(userRole.toString()));
    }

}
