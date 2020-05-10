package by.beg.payment_system.controller;

import by.beg.payment_system.dto.AuthenticationRequestDTO;
import by.beg.payment_system.dto.AuthenticationResponseDTO;
import by.beg.payment_system.dto.UserResponseDTO;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //USER

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDTO> registration(@RequestBody @Valid User user) throws UserIsPresentException {
        return ResponseEntity.ok(userService.registration(user));
    }


    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponseDTO> authentication(@RequestBody @Valid AuthenticationRequestDTO user) throws UserNotFoundException {
        return ResponseEntity.ok(userService.authentication(user));
    }


    //ADMIN

    @GetMapping("/admin/findByEmail/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/admin/findById/{userId}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable long userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/admin/findByPassport/{passport}")
    public ResponseEntity<UserResponseDTO> findByPassport(@PathVariable String passport) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findByPassport(passport));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<String> updateUser(@RequestBody @Valid User user) throws UserNotFoundException {
        userService.updateUser(user);
        return ResponseEntity.ok("User with id = " + user.getId() + " has been updated");
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) throws UserNotFoundException, UnremovableStatusException {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with id = " + userId + " has been deleted");
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("/admin/establishAdminRole/{userId}")
    public ResponseEntity<String> establishRole(@PathVariable long userId) throws UserNotFoundException {
        userService.establishAdminRole(userId);
        return ResponseEntity.ok("User's role with id = " + userId + " has been changed");
    }

    @GetMapping("/admin/findByWalletValue/{value}")
    public ResponseEntity<UserResponseDTO> findByWalletValue(@PathVariable String value) throws UserNotFoundException, WalletNotFoundException {
        return ResponseEntity.ok(userService.findByWalletValue(value));
    }

    @PutMapping("/admin/changeStatus/{userId}/{status}")
    public ResponseEntity<String> changeStatus(@PathVariable long userId, @PathVariable Status status) throws UserNotFoundException {
        userService.changeStatus(userId, status);
        return ResponseEntity.ok("User's status with id = " + userId + " has been changed");
    }

}
