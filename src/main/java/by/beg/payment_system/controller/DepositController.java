package by.beg.payment_system.controller;

import by.beg.payment_system.dto.DateDTO;
import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.user_exception.NoAccessException;
import by.beg.payment_system.exception.user_exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.enumerations.DepositStatus;
import by.beg.payment_system.model.user.User;
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

    @PostMapping("/create")
    public ResponseEntity<Deposit> createDeposit(@RequestHeader String token, @RequestBody @Valid DepositOpenDTO openDTO) throws UserIsNotAuthorizedException, LackOfMoneyException, WalletNotFoundException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(depositService.create(openDTO, user));
    }

    @GetMapping("/getDescriptions")
    public ResponseEntity<List<Deposit>> getDesc(@RequestHeader String token) throws UserIsNotAuthorizedException {
        userService.checkAuthorization(token);
        return ResponseEntity.ok().body(depositService.getDepositsDescription());
    }

    @GetMapping("/getAllByUser")
    public ResponseEntity<List<Deposit>> getAllByUser(@RequestHeader String token) throws UserIsNotAuthorizedException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok().body(depositService.getAllByUser(user));
    }

    @PutMapping("/pickUp")
    public ResponseEntity<List<Deposit>> pickUpDeposits(@RequestHeader String token) throws UserIsNotAuthorizedException, WalletNotFoundException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok().body(depositService.pickUp(user));
    }

    //ADMIN

    @GetMapping("/getAllByUser/{userId}")
    public ResponseEntity<List<Deposit>> adminGetAllByUser(@RequestHeader String token, @PathVariable long userId) throws UserIsNotAuthorizedException, NoAccessException {
        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(depositService.getAllForAdmin(userId));
    }

    @GetMapping("/getAllByStatus/{status}")
    public ResponseEntity<List<Deposit>> adminGetAllByStatus(@RequestHeader String token, @PathVariable DepositStatus status) throws UserIsNotAuthorizedException, NoAccessException {
        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(depositService.getAllForAdminByStatus(status));
    }

    @PostMapping("/getAll/filterByCreateDate")
    public ResponseEntity<List<Deposit>> adminFilterByDate(@RequestHeader String token, @RequestBody DateDTO dateDTO) throws UserIsNotAuthorizedException, NoAccessException {
        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(depositService.filterByCreateDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));

    }


    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(User.UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }


}
