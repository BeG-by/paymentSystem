package by.beg.payment_system.service;

import by.beg.payment_system.dto.AuthenticationResponseDTO;
import by.beg.payment_system.dto.AuthenticationRequestDTO;
import by.beg.payment_system.dto.UserResponseDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;

import java.util.List;

public interface UserService {

    UserResponseDTO registration(User user) throws UserIsPresentException;

    AuthenticationResponseDTO authentication(AuthenticationRequestDTO user) throws UserNotFoundException;

    UserResponseDTO findById(long id) throws UserNotFoundException;

    UserResponseDTO findByEmail(String email) throws UserNotFoundException;

    UserResponseDTO findByPassport(String passport) throws UserNotFoundException;

    void updateUser(User user) throws UserNotFoundException;

    void deleteUser(long userId) throws UserNotFoundException, UnremovableStatusException;

    List<UserResponseDTO> findAllUsers();

    void establishAdminRole(long userId) throws UserNotFoundException;

    UserResponseDTO findByWalletValue(String walletValue) throws WalletNotFoundException, UserNotFoundException;

    void changeStatus(long userId, Status status) throws UserNotFoundException;

    User findCurrentUser(String email) throws UserNotFoundException;

}
