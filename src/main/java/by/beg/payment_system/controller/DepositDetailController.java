package by.beg.payment_system.controller;

import by.beg.payment_system.dto.DateFilterRequestDTO;
import by.beg.payment_system.dto.DepositOpenRequestDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.service.DepositDetailService;
import by.beg.payment_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/depositDetail")
@Validated
public class DepositDetailController {

    private DepositDetailService depositDetailService;
    private UserService userService;

    @Autowired
    public DepositDetailController(DepositDetailService depositDetailService, UserService userService) {
        this.depositDetailService = depositDetailService;
        this.userService = userService;
    }

    //USER

    @PostMapping("/create")
    public ResponseEntity<String> createDeposit(@RequestBody @Valid DepositOpenRequestDTO openDTO, Principal principal)
            throws LackOfMoneyException, WalletNotFoundException, DepositNotFoundException, CurrencyConverterException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        depositDetailService.create(openDTO, currentUser);
        return ResponseEntity.ok("DepositDetail has been successfully create");
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DepositDetail>> findAllByUser(Principal principal) throws UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(depositDetailService.findAllByUser(currentUser));
    }

    @PutMapping("/pickUp")
    public ResponseEntity<List<DepositDetail>> pickUpDeposits(Principal principal) throws WalletNotFoundException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(depositDetailService.pickUp(currentUser));
    }


    //ADMIN

    @GetMapping("/admin/findAll/{userId}")
    public ResponseEntity<List<DepositDetail>> findAllById(@PathVariable long userId) throws UserNotFoundException {
        return ResponseEntity.ok(depositDetailService.findAllById(userId));
    }

    @GetMapping("/admin/findAllByStatus/{status}")
    public ResponseEntity<List<DepositDetail>> adminGetAllByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(depositDetailService.findAllByStatus(status));
    }

    @PostMapping("/admin/findAllBetweenDate")
    public ResponseEntity<List<DepositDetail>> adminFilterByDate(@RequestBody @Valid DateFilterRequestDTO dateDTO) {
        return ResponseEntity.ok(depositDetailService.findAllBetweenDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

    @DeleteMapping("/admin/deleteById/{depositId}")
    public ResponseEntity<String> delete(@PathVariable long depositId) throws DepositNotFoundException, UnremovableStatusException {
        depositDetailService.deleteById(depositId);
        return ResponseEntity.ok("DepositDetail with id = " + depositId + " has been deleted");
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<DepositDetail>> deleteAll() {
        return ResponseEntity.ok(depositDetailService.deleteAll());
    }

    @PutMapping("/admin/refreshAll")
    public ResponseEntity<String> refreshAll() {
        depositDetailService.refreshAll();
        return ResponseEntity.ok("DepositsDetails have been refreshed");
    }

}
