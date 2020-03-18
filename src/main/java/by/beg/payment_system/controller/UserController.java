package by.beg.payment_system.controller;

import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.model.User;
import by.beg.payment_system.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@Validated
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody @Valid User user) throws UserIsPresentException {
        System.out.println(user);
        return new ResponseEntity<>(userService.registration(user), HttpStatus.OK);
    }

}
