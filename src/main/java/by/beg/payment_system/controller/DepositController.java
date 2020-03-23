package by.beg.payment_system.controller;

import by.beg.payment_system.dto.DepositOpenDTO;
import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.user_exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.Deposit;
import by.beg.payment_system.model.User;
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

    @GetMapping("/create")
    public ResponseEntity<Deposit> createDeposit(@RequestHeader String token, @RequestBody @Valid DepositOpenDTO openDTO) throws UserIsNotAuthorizedException, LackOfMoneyException, WalletNotFoundException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(depositService.create(openDTO, user));
    }

    @GetMapping("/getDesc")
    public ResponseEntity<List<Deposit>> getDesc(@RequestHeader String token) throws UserIsNotAuthorizedException {
        userService.checkAuthorization(token);
        return ResponseEntity.ok().body(depositService.getDepositDescription());
    }

}
