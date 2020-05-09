package by.beg.payment_system.service.impl;

import by.beg.payment_system.dto.AuthenticationRequestDTO;
import by.beg.payment_system.dto.AuthenticationResponseDTO;
import by.beg.payment_system.dto.UserResponseDTO;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.security.JwtTokenProvider;
import by.beg.payment_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           WalletRepository walletRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserResponseDTO registration(User user) throws UserIsPresentException {

        if (userRepository.findUserByEmailOrPassport(user.getEmail(), user.getPassport()).isPresent()) {
            throw new UserIsPresentException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saveUser = userRepository.save(user);
        log.info("User was registered: " + saveUser);
        return UserResponseDTO.fromUser(saveUser);
    }

    @Override
    public AuthenticationResponseDTO authentication(AuthenticationRequestDTO user) throws UserNotFoundException {

        String email = user.getEmail();
        String password = user.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User realUser = userRepository.findUserByEmail(user.getEmail()).orElseThrow(UserNotFoundException::new);

        String token = jwtTokenProvider.createToken(email, realUser.getUserRole());
        return new AuthenticationResponseDTO(email, token);
    }


    @Override
    public UserResponseDTO findById(long id) throws UserNotFoundException {
        return userRepository.findById(id).map(UserResponseDTO::fromUser).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserResponseDTO findByEmail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email).map(UserResponseDTO::fromUser).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserResponseDTO findByPassport(String passport) throws UserNotFoundException {
        return userRepository.findUserByPassport(passport).map(UserResponseDTO::fromUser).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void updateUser(User user) throws UserNotFoundException {
        User persistUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User was updated: " + persistUser);
    }

    @Override
    public void deleteUser(long userId) throws UserNotFoundException, UnremovableStatusException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        long walletCount = user.getWallets().stream().filter(wallet -> wallet.getBalance().compareTo(BigDecimal.ZERO) > 0).count();
        long depositCount = user.getDepositDetails().stream().filter(depositDetail -> !depositDetail.getDepositDetailStatus().equals(Status.DELETED)).count();
        long creditCount = user.getCreditDetails().stream().filter(creditDetail -> !creditDetail.getCreditStatus().equals(Status.CLOSED)).count();

        if (walletCount > 0 || depositCount > 0 || creditCount > 0) {
            throw new UnremovableStatusException();
        }

        user.getTransferDetails().forEach(transferDetail -> transferDetail.setUser(null));

        userRepository.delete(user);
        log.info("User was deleted: " + user);
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDTO::fromUser).collect(Collectors.toList());
    }

    @Override
    public void establishAdminRole(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setUserRole(UserRole.ADMIN);
        user.setLastUpdate(LocalDateTime.now());
        log.info("Admin role was added for: " + user);
    }

    @Override
    public UserResponseDTO findByWalletValue(String walletValue) throws WalletNotFoundException, UserNotFoundException {
        Wallet wallet = walletRepository.findWalletByWalletValue(walletValue).orElseThrow(WalletNotFoundException::new);
        return userRepository.findUserByWallets(wallet).map(UserResponseDTO::fromUser).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void changeStatus(long userId, Status status) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.setStatus(status);
        user.setLastUpdate(LocalDateTime.now());
        log.info("Status was changed for: " + user);
    }

}
