package by.beg.payment_system.controller;

import by.beg.payment_system.dto.CreditOpenDTO;
import by.beg.payment_system.dto.DateDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.service.CreditDetailService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/creditDetail")
@Validated
public class CreditDetailController {

    private CreditDetailService creditDetailService;
    private UserService userService;

    public CreditDetailController(CreditDetailService creditDetailService, UserService userService) {
        this.creditDetailService = creditDetailService;
        this.userService = userService;
    }

    //USER

    @PostMapping("/create")
    public ResponseEntity<CreditDetail> create(@RequestHeader String token, @RequestBody @Valid CreditOpenDTO creditOpenDTO)
            throws UserIsNotAuthorizedException, WalletNotFoundException, CreditNotFoundException, CreditDetailIsPresentException, UserBlockedException {

        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(creditDetailService.create(creditOpenDTO, user));
    }

    @GetMapping("/get")
    public ResponseEntity<CreditDetail> get(@RequestHeader String token)
            throws UserIsNotAuthorizedException, CreditNotFoundException, UserBlockedException {

        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(creditDetailService.getByUser(user));
    }

    @PostMapping("/repay")
    public ResponseEntity<CreditDetail> repay(@RequestHeader String token, @RequestBody @Valid CreditOpenDTO creditOpenDTO)
            throws UserIsNotAuthorizedException, CreditNotFoundException, LackOfMoneyException, WalletNotFoundException, UserBlockedException {

        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(creditDetailService.repayDebt(user, creditOpenDTO));
    }

    //ADMIN

    @GetMapping("/get/{userId}")
    public ResponseEntity<CreditDetail> getForAdmin(@RequestHeader String token, @PathVariable long userId)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException, UserNotFoundException, CreditNotFoundException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditDetailService.getForAdmin(userId));
    }

    @GetMapping("/getAllByStatus/{status}")
    public ResponseEntity<List<CreditDetail>> getByStatus(@RequestHeader String token, @PathVariable Status status)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditDetailService.getAllForAdminByStatus(status));
    }

    @PostMapping("/getAll/filterByCreateDate")
    public ResponseEntity<List<CreditDetail>> getByDate(@RequestHeader String token, @RequestBody @Valid DateDTO dateDTO)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditDetailService.filterByCreateDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

    @DeleteMapping("/delete/{creditId}")
    public ResponseEntity<CreditDetail> delete(@RequestHeader String token, @PathVariable long creditId)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException, CreditNotFoundException, UnremovableStatusException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditDetailService.delete(creditId));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<List<CreditDetail>> deleteAll(@RequestHeader String token)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok(creditDetailService.deleteAll());
    }

    @PutMapping("/refreshAll")
    public ResponseEntity<String> refreshAll(@RequestHeader String token)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        creditDetailService.refreshAll();
        return ResponseEntity.ok().body("CreditDetails were refreshed");

    }

    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }


}
