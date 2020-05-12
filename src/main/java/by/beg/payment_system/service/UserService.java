package by.beg.payment_system.service;

import by.beg.payment_system.dto.request.AuthenticationRequestDTO;
import by.beg.payment_system.dto.response.AuthenticationResponseDTO;
import by.beg.payment_system.dto.response.UserResponseDTO;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;

import java.util.List;

public interface UserService {

    UserResponseDTO register(User user);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO user);

    UserResponseDTO findById(long id);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO findByPassport(String passport);

    void updateUser(User user);

    void deleteUser(long userId);

    List<UserResponseDTO> findAllUsers();

    void establishAdminRole(long userId);

    UserResponseDTO findByWalletValue(String walletValue);

    void changeStatus(long userId, Status status);

    User findCurrentUser(String email);

}
