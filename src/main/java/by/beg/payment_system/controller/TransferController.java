package by.beg.payment_system.controller;

import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.transfer_exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.user_exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import by.beg.payment_system.model.TransferDetail;
import by.beg.payment_system.model.User;
import by.beg.payment_system.service.TransferService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/transfer")
@Validated
public class TransferController {

    private UserService userService;
    private TransferService transferService;

    public TransferController(UserService userService, TransferService transferService) {
        this.userService = userService;
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferDetail> transfer(@RequestHeader String token, @RequestBody @Valid TransferDetail transferDetail)
            throws UserIsNotAuthorizedException, WalletNotFoundException, TargetWalletNotFoundException, LackOfMoneyException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(transferService.doTransfer(user, transferDetail));
    }

    @GetMapping("/getExchangeRates")
    public ResponseEntity<Map<String, Double>> getRates(@RequestHeader String token) throws UserIsNotAuthorizedException {
        userService.checkAuthorization(token);
        return ResponseEntity.ok(transferService.getExchangeRates());
    }
}
