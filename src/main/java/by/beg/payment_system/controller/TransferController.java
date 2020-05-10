package by.beg.payment_system.controller;

import by.beg.payment_system.dto.DateFilterRequestDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.service.TransferService;
import by.beg.payment_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transfer")
@Validated
public class TransferController {

    private UserService userService;
    private TransferService transferService;

    @Autowired
    public TransferController(UserService userService, TransferService transferService) {
        this.userService = userService;
        this.transferService = transferService;
    }

    //USER

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody @Valid TransferDetail transferDetail, Principal principal)
            throws WalletNotFoundException, TargetWalletNotFoundException, LackOfMoneyException, CurrencyConverterException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        transferService.makeTransfer(currentUser, transferDetail);
        return ResponseEntity.ok("Transfer to wallet " + transferDetail.getTargetWalletValue() + " has been successfully made ");
    }

    @GetMapping("/findExchangeRates")
    public ResponseEntity<Map<String, BigDecimal>> findAllRates() throws CurrencyConverterException {
        return ResponseEntity.ok(transferService.findAllRates());
    }

    //ADMIN

    @PostMapping("/admin/findAllBetweenDate")
    public ResponseEntity<List<TransferDetail>> filterByDate(@RequestBody @Valid DateFilterRequestDTO dateDTO) {
        return ResponseEntity.ok(transferService.findAllBetweenDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<TransferDetail>> findAll() {
        return ResponseEntity.ok(transferService.findAll());
    }

    @DeleteMapping("/admin/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) throws TransferNotFoundException {
        transferService.deleteById(id);
        return ResponseEntity.ok("Transfer with id = " + id + " was deleted");
    }

    @DeleteMapping("/admin/deleteAllBetweenDate")
    public ResponseEntity<List<TransferDetail>> deleteAll(@RequestBody @Valid DateFilterRequestDTO dateDTO) {
        return ResponseEntity.ok(transferService.deleteBetweenDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

}
