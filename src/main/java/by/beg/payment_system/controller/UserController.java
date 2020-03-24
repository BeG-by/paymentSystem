package by.beg.payment_system.controller;

import by.beg.payment_system.dto.UserAuthorizationDTO;
import by.beg.payment_system.exception.user_exception.NoAccessException;
import by.beg.payment_system.exception.user_exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.user_exception.UserIsPresentException;
import by.beg.payment_system.exception.user_exception.UserNotFoundException;
import by.beg.payment_system.model.security.Token;
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
    public ResponseEntity<User> registration(@RequestBody @Valid User user) throws UserIsPresentException {
        return new ResponseEntity<>(userService.registration(user), HttpStatus.OK);
    }


    @PostMapping("/authorization")
    public ResponseEntity<Token> authorization(@RequestBody @Valid UserAuthorizationDTO user) throws UserNotFoundException {
        return new ResponseEntity<>(userService.authorization(user), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<User> logout(@RequestHeader String token) throws UserNotFoundException, UserIsNotAuthorizedException {
        return new ResponseEntity<>(userService.logout(token), HttpStatus.OK);

    }


    //ADMIN

    @GetMapping("findByEmail/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email, @RequestHeader String token) throws UserNotFoundException, NoAccessException, UserIsNotAuthorizedException {
        User user = userService.checkAuthorization(token);
        checkAdminRole(user);
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    @GetMapping("findById/{userId}")
    public ResponseEntity<User> findById(@PathVariable long userId, @RequestHeader String token) throws UserNotFoundException, NoAccessException, UserIsNotAuthorizedException {
        User user = userService.checkAuthorization(token);
        checkAdminRole(user);
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    }

    @GetMapping("findByPassport/{passport}")
    public ResponseEntity<User> findByPassport(@PathVariable String passport, @RequestHeader String token) throws UserNotFoundException, NoAccessException, UserIsNotAuthorizedException {
        User user = userService.checkAuthorization(token);
        checkAdminRole(user);
        return new ResponseEntity<>(userService.findByPassport(passport), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user, @RequestHeader String token) throws UserIsNotAuthorizedException, NoAccessException, UserNotFoundException {
        checkAdminRole(userService.checkAuthorization(token));
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable long userId, @RequestHeader String token) throws UserIsNotAuthorizedException, NoAccessException, UserNotFoundException {
        checkAdminRole(userService.checkAuthorization(token));
        return new ResponseEntity<>(userService.deleteUser(userId), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getUsers(@RequestHeader String token) throws UserIsNotAuthorizedException, NoAccessException {
        checkAdminRole(userService.checkAuthorization(token));
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/getAdminRole/{userId}")
    public ResponseEntity<User> getAdminRoleUser(@PathVariable long userId, @RequestHeader String token) throws UserIsNotAuthorizedException, NoAccessException, UserNotFoundException {
        checkAdminRole(userService.checkAuthorization(token));
        return new ResponseEntity<>(userService.getAdminRole(userId), HttpStatus.OK);
    }


    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(User.UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }


}
