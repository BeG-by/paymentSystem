package by.beg.payment_system.controller;

import by.beg.payment_system.dto.DateDTO;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.NoAccessException;
import by.beg.payment_system.exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.service.TransferService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    //USER

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

    //ADMIN

    @PostMapping("/getAll/filterByDate")
    public ResponseEntity<List<TransferDetail>> filterByDate(@RequestHeader String token, @RequestBody @Valid DateDTO dateDTO)
            throws UserIsNotAuthorizedException, NoAccessException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(transferService.filterByDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }

}
