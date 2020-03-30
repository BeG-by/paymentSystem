package by.beg.payment_system.controller;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.service.DepositService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/deposit")
@Validated
public class DepositController {

    private DepositService depositService;
    private UserService userService;

    public DepositController(DepositService depositService, UserService userService) {
        this.depositService = depositService;
        this.userService = userService;
    }

    //USER

    @GetMapping("/getAllAvailable")
    public ResponseEntity<List<Deposit>> getAllAvailable(@RequestHeader String token) throws UserIsNotAuthorizedException, UserBlockedException {
        userService.checkAuthorization(token);
        return ResponseEntity.ok(depositService.getAllAvailable());
    }


    //ADMIN

    @PostMapping("/create")
    public ResponseEntity<Deposit> create(@RequestHeader String token, @RequestBody @Valid Deposit deposit)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException, DepositIsPresentException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(depositService.create(deposit));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Deposit>> getAll(@RequestHeader String token)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(depositService.getAll());
    }

    @GetMapping("/findById/{depositId}")
    public ResponseEntity<Deposit> getAll(@RequestHeader String token, @PathVariable long depositId)
            throws UserIsNotAuthorizedException, NoAccessException, DepositNotFoundException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(depositService.findById(depositId));
    }

    @PutMapping("/update")
    public ResponseEntity<Deposit> update(@RequestHeader String token, @RequestBody @Valid Deposit deposit)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException, DepositNotFoundException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(depositService.update(deposit));
    }


    @DeleteMapping("/delete/{depositId}")
    public ResponseEntity<Deposit> delete(@RequestHeader String token, @PathVariable long depositId)
            throws UserIsNotAuthorizedException, NoAccessException, DepositNotFoundException, UnremovableStatusException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(depositService.delete(depositId));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<List<Deposit>> deleteAll(@RequestHeader String token) throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {
        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(depositService.deleteAll());
    }

    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }

}
