package by.beg.payment_system.controller;

import by.beg.payment_system.exception.NoAccessException;
import by.beg.payment_system.exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.WalletIsExistException;
import by.beg.payment_system.model.User;
import by.beg.payment_system.model.Wallet;
import by.beg.payment_system.service.UserService;
import by.beg.payment_system.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
@RequestMapping("/wallet")
public class WalletController {

    private UserService userService;
    private WalletService walletService;

    public WalletController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/create/{type}")
    public ResponseEntity<Wallet> addWallet(@PathVariable Wallet.WalletType type, @RequestHeader String token) throws UserIsNotAuthorizedException, WalletIsExistException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(walletService.create(type, user));

    }

    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(User.UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }
}
