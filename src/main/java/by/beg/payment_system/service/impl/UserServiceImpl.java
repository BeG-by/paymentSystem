package by.beg.payment_system.service.impl;

import by.beg.payment_system.dto.request.AuthenticationRequestDTO;
import by.beg.payment_system.dto.response.AuthenticationResponseDTO;
import by.beg.payment_system.dto.response.UserResponseDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.security.jwt.JwtTokenProvider;
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

import static by.beg.payment_system.service.util.MessageConstant.*;

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
    public UserResponseDTO register(User user) {

        if (userRepository.findUserByEmailOrPassport(user.getEmail(), user.getPassport()).isPresent()) {
            throw new ExistException(USER_EXIST_MESSAGE);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saveUser = userRepository.save(user);
        log.info("User was registered: " + saveUser);
        return UserResponseDTO.fromUser(saveUser);
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO user) {

        String email = user.getEmail();
        String password = user.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User realUser = userRepository.findUserByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        String token = jwtTokenProvider.createToken(email, realUser.getUserRole());
        return new AuthenticationResponseDTO(email, token);
    }


    @Override
    public UserResponseDTO findById(long id) {
        return userRepository.findById(id).map(UserResponseDTO::fromUser)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        return userRepository.findUserByEmail(email).map(UserResponseDTO::fromUser)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public UserResponseDTO findByPassport(String passport) {
        return userRepository.findUserByPassport(passport).map(UserResponseDTO::fromUser)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public void updateUser(User user) {
        User persistUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getAddress().setId(persistUser.getAddress().getId());

        userRepository.save(user);
        log.info("User was updated: " + user);
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));

        long walletCount = user.getWallets().stream()
                .filter(wallet -> wallet.getBalance().compareTo(BigDecimal.ZERO) > 0).count();

        long depositCount = user.getDepositDetails().stream()
                .filter(depositDetail -> !depositDetail.getDepositDetailStatus().equals(Status.CLOSED)).count();

        long creditCount = user.getCreditDetails().stream()
                .filter(creditDetail -> !creditDetail.getCreditStatus().equals(Status.CLOSED)).count();

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
    public void establishAdminRole(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        user.setUserRole(UserRole.ADMIN);
        user.setLastModified(LocalDateTime.now());
        log.info("Admin role was added for: " + user);
    }

    @Override
    public UserResponseDTO findByWalletValue(String walletValue) {
        Wallet wallet = walletRepository.findWalletByWalletValue(walletValue)
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND_MESSAGE));
        return userRepository.findUserByWallets(wallet).map(UserResponseDTO::fromUser)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public void changeStatus(long userId, Status status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        user.setStatus(status);
        user.setLastModified(LocalDateTime.now());
        log.info("Status was changed for: " + user);
    }

    @Override
    public User findCurrentUser(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

}
