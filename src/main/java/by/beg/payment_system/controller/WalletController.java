package by.beg.payment_system.controller;

import by.beg.payment_system.dto.ChargeWalletDTO;
import by.beg.payment_system.exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.finance.enumerations.CurrencyType;
import by.beg.payment_system.service.UserService;
import by.beg.payment_system.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestControllerAdvice
@RequestMapping("/wallet")
@Validated
public class WalletController {

    private UserService userService;
    private WalletService walletService;

    public WalletController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/create/{type}")
    public ResponseEntity<Wallet> addWallet(@PathVariable CurrencyType type, @RequestHeader String token) throws UserIsNotAuthorizedException, WalletIsExistException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(walletService.create(type, user));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Wallet>> getWallets(@RequestHeader String token) throws UserIsNotAuthorizedException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(walletService.getAll(user));
    }


    @DeleteMapping("/delete/{type}")
    public ResponseEntity<Wallet> deleteWallet(@PathVariable CurrencyType type, @RequestHeader String token) throws UserIsNotAuthorizedException, WalletNotFoundException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(walletService.delete(type, user));
    }

    @PostMapping("/rechargeBalance")
    public ResponseEntity<Wallet> recharge(@RequestBody @Valid ChargeWalletDTO chargeWalletDTO, @RequestHeader String token) throws UserIsNotAuthorizedException, WalletNotFoundException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(walletService.recharge(user, chargeWalletDTO.getType(), chargeWalletDTO.getMoney()));
    }

}
