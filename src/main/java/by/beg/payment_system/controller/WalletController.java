package by.beg.payment_system.controller;

import by.beg.payment_system.dto.ChargeWalletRequestDTO;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.service.UserService;
import by.beg.payment_system.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestControllerAdvice
@RequestMapping("/wallet")
@Validated
public class WalletController {

    private UserService userService;
    private WalletService walletService;

    @Autowired
    public WalletController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/create/{type}")
    public ResponseEntity<Wallet> createWallet(@PathVariable CurrencyType type, Principal principal)
            throws WalletIsExistException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(walletService.create(type, currentUser));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Wallet>> findAllWallets(Principal principal) throws UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(walletService.findAll(currentUser));
    }


    @DeleteMapping("/delete/{type}")
    public ResponseEntity<String> deleteWallet(@PathVariable CurrencyType type, Principal principal)
            throws WalletNotFoundException, UnremovableStatusException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        walletService.delete(type, currentUser);
        return ResponseEntity.ok(type + " wallet has been deleted");
    }

    @PostMapping("/recharge")
    public ResponseEntity<Wallet> rechargeBalance(@RequestBody @Valid ChargeWalletRequestDTO requestDTO, Principal principal)
            throws WalletNotFoundException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(walletService.recharge(currentUser, requestDTO.getType(), requestDTO.getMoney()));
    }

    @PutMapping("/clear/{type}")
    public ResponseEntity<String> clearBalance(@PathVariable CurrencyType type, Principal principal)
            throws WalletNotFoundException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        walletService.clear(type, currentUser);
        return ResponseEntity.ok("Balance of " + type + " wallet is zero");
    }

}
