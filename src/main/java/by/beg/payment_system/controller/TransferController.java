package by.beg.payment_system.controller;

import by.beg.payment_system.dto.DateDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.service.TransferService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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
            throws UserIsNotAuthorizedException, WalletNotFoundException, TargetWalletNotFoundException, LackOfMoneyException, CurrencyConverterException, UserBlockedException {
        User user = userService.checkAuthorization(token);
        return ResponseEntity.ok(transferService.doTransfer(user, transferDetail));
    }

    @GetMapping("/getExchangeRates")
    public ResponseEntity<Map<String, BigDecimal>> getRates(@RequestHeader String token)
            throws UserIsNotAuthorizedException, CurrencyConverterException, UserBlockedException {

        userService.checkAuthorization(token);
        return ResponseEntity.ok(transferService.getExchangeRates());
    }

    //ADMIN

    @PostMapping("/getAll/filterByDate")
    public ResponseEntity<List<TransferDetail>> filterByDate(@RequestHeader String token, @RequestBody @Valid DateDTO dateDTO)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(transferService.filterByDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TransferDetail>> getAll(@RequestHeader String token)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(transferService.getAll());
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<TransferDetail> deleteById(@RequestHeader String token, @PathVariable long id)
            throws UserIsNotAuthorizedException, NoAccessException, TransferNotFoundException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(transferService.deleteById(id));
    }

    @DeleteMapping("/deleteAllByDate")
    public ResponseEntity<List<TransferDetail>> deleteAll(@RequestHeader String token, @RequestBody @Valid DateDTO dateDTO)
            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {

        checkAdminRole(userService.checkAuthorization(token));
        return ResponseEntity.ok().body(transferService.deleteBetweenDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }


    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }

}
