package by.beg.payment_system.controller;

import by.beg.payment_system.dto.AuthenticationRequestDTO;
import by.beg.payment_system.dto.AuthenticationResponseDTO;
import by.beg.payment_system.dto.UserResponseDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.service.UserService;
import org.springframework.http.HttpStatus;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //USER

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDTO> registration(@RequestBody @Valid User user) throws UserIsPresentException {
        return new ResponseEntity<>(userService.registration(user), HttpStatus.OK);
    }


    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponseDTO> authentication(@RequestBody @Valid AuthenticationRequestDTO user) throws UserNotFoundException {
        return new ResponseEntity<>(userService.authentication(user), HttpStatus.OK);
    }


    //ADMIN

    @GetMapping("/admin/findByEmail/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/admin/findById/{userId}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable long userId) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    }

    @GetMapping("/admin/findByPassport/{passport}")
    public ResponseEntity<UserResponseDTO> findByPassport(@PathVariable String passport) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findByPassport(passport), HttpStatus.OK);
    }

    @PutMapping("/admin/update") // -encode ?
    public ResponseEntity<String> updateUser(@RequestBody @Valid User user) throws UserNotFoundException {
        userService.updateUser(user);
        return new ResponseEntity<>("User with id = " + user.getId() + " has been updated", HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) throws UserNotFoundException, UnremovableStatusException {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User with id = " + userId + " has been deleted", HttpStatus.OK);
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/admin/establishAdminRole/{userId}")
    public ResponseEntity<String> establishRole(@PathVariable long userId) throws UserNotFoundException {
        userService.establishAdminRole(userId);
        return new ResponseEntity<>("User's role with id = " + userId + " has been changed", HttpStatus.OK);
    }

    @GetMapping("/admin/findByWalletValue/{value}")
    public ResponseEntity<UserResponseDTO> findByWalletValue(@PathVariable String value) throws UserNotFoundException, WalletNotFoundException {
        return new ResponseEntity<>(userService.findByWalletValue(value), HttpStatus.OK);
    }

    @PutMapping("/admin/changeStatus/{userId}/{status}")
    public ResponseEntity<String> changeStatus(@PathVariable long userId, @PathVariable Status status) throws UserNotFoundException {
        userService.changeStatus(userId, status);
        return new ResponseEntity<>("User's status with id = " + userId + " has been changed", HttpStatus.OK);
    }

}
