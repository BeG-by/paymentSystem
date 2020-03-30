package by.beg.payment_system.controller;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.service.CreditService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/credit")
@Validated
public class CreditController {

    private CreditService creditService;
    private UserService userService;

    public CreditController(CreditService creditService, UserService userService) {
        this.creditService = creditService;
        this.userService = userService;
    }

    //USER

    @GetMapping("/getAllAvailable")
    public ResponseEntity<List<Credit>> getAllAvailable(@RequestHeader String token) throws UserIsNotAuthorizedException, UserBlockedException {
        userService.checkAuthorization(token);
        return ResponseEntity.ok(creditService.getAllAvailable());
    }


    //ADMIN

    @PostMapping("/create")
    public ResponseEntity<Credit> create(@RequestHeader String token, @RequestBody @Valid Credit credit)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException, CreditIsPresentException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditService.create(credit));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Credit>> getAll(@RequestHeader String token)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditService.getAll());
    }

    @GetMapping("/findById/{creditId}")
    public ResponseEntity<Credit> getAll(@RequestHeader String token, @PathVariable long creditId)
            throws UserIsNotAuthorizedException, NoAccessException, CreditNotFoundException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditService.findById(creditId));
    }

    @PutMapping("/update")
    public ResponseEntity<Credit> update(@RequestHeader String token, @RequestBody @Valid Credit credit)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException, CreditNotFoundException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditService.update(credit));
    }

    @DeleteMapping("/delete/{creditId}")
    public ResponseEntity<Credit> delete(@RequestHeader String token, @PathVariable long creditId)
            throws UserIsNotAuthorizedException, NoAccessException, UnremovableStatusException, CreditNotFoundException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditService.delete(creditId));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<List<Credit>> deleteAll(@RequestHeader String token)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditService.deleteAll());
    }

    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }

}
