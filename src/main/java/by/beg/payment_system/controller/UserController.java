package by.beg.payment_system.controller;

import by.beg.payment_system.model.User;
import by.beg.payment_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody User user) {
        User registration = userService.registration(user);
        return new ResponseEntity<>(registration, HttpStatus.OK);
    }

}
