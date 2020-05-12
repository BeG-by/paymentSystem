package by.beg.payment_system.controller;

import by.beg.payment_system.dto.request.AuthenticationRequestDTO;
import by.beg.payment_system.dto.response.AuthenticationResponseDTO;
import by.beg.payment_system.dto.response.MessageResponseDTO;
import by.beg.payment_system.dto.response.UserResponseDTO;
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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registration(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.register(user));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authentication(@RequestBody @Valid AuthenticationRequestDTO user) {
        return ResponseEntity.ok(userService.authenticate(user));
    }


    //ADMIN

    @GetMapping("/admin/findByEmail/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/admin/findById/{userId}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/admin/findByPassport/{passport}")
    public ResponseEntity<UserResponseDTO> findByPassport(@PathVariable String passport) {
        return ResponseEntity.ok(userService.findByPassport(passport));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<MessageResponseDTO> updateUser(@RequestBody @Valid User user) {
        userService.updateUser(user);
        MessageResponseDTO message = new MessageResponseDTO("User with id = " + user.getId() + " has been updated");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteById/{userId}")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        MessageResponseDTO message = new MessageResponseDTO("User with id = " + userId + " has been deleted");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("/admin/establishAdminRole/{userId}")
    public ResponseEntity<MessageResponseDTO> establishRole(@PathVariable long userId) {
        userService.establishAdminRole(userId);
        MessageResponseDTO message = new MessageResponseDTO("Role of user with id = " + userId + " has been changed");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/findByWalletValue/{value}")
    public ResponseEntity<UserResponseDTO> findByWalletValue(@PathVariable String value) {
        return ResponseEntity.ok(userService.findByWalletValue(value));
    }

    @PutMapping("/admin/changeStatus/{userId}/{status}")
    public ResponseEntity<MessageResponseDTO> changeStatus(@PathVariable long userId, @PathVariable Status status) {
        userService.changeStatus(userId, status);
        MessageResponseDTO message = new MessageResponseDTO("Status of user with id = " + userId + " has been changed");
        return ResponseEntity.ok(message);
    }

}
