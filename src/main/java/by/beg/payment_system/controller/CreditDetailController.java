package by.beg.payment_system.controller;

import by.beg.payment_system.dto.CreditOpenRequestDTO;
import by.beg.payment_system.dto.DateFilterRequestDTO;
import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.service.CreditDetailService;
import by.beg.payment_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/creditDetail")
@Validated
public class CreditDetailController {

    private CreditDetailService creditDetailService;
    private UserService userService;

    @Autowired
    public CreditDetailController(CreditDetailService creditDetailService, UserService userService) {
        this.creditDetailService = creditDetailService;
        this.userService = userService;
    }

    //USER

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid CreditOpenRequestDTO openDTO, Principal principal)
            throws WalletNotFoundException, CreditNotFoundException, CreditDetailIsPresentException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        creditDetailService.create(openDTO, currentUser);
        return ResponseEntity.ok("CreditDetail has been successfully create");
    }

    @GetMapping("/find")
    public ResponseEntity<CreditDetail> find(Principal principal) throws CreditNotFoundException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(creditDetailService.findByUser(currentUser));
    }

    @PostMapping("/repay")
    public ResponseEntity<CreditDetail> repay(@RequestBody @Valid CreditOpenRequestDTO openDTO, Principal principal)
            throws CreditNotFoundException, LackOfMoneyException, WalletNotFoundException, UserNotFoundException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(creditDetailService.repayDebt(currentUser, openDTO));
    }

    //ADMIN

    @GetMapping("/admin/findById/{userId}")
    public ResponseEntity<CreditDetail> findById(@PathVariable long userId)
            throws UserNotFoundException, CreditNotFoundException {
        return ResponseEntity.ok(creditDetailService.findByUserId(userId));
    }

    @GetMapping("/admin/findAllByStatus/{status}")
    public ResponseEntity<List<CreditDetail>> findAllByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(creditDetailService.findAllByStatus(status));
    }

    @PostMapping("/admin/findAllBetweenDate")
    public ResponseEntity<List<CreditDetail>> findByDate(@RequestBody @Valid DateFilterRequestDTO dateDTO) {
        return ResponseEntity.ok(creditDetailService.findAllBetweenDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

    @DeleteMapping("/admin/deleteById/{creditId}")
    public ResponseEntity<String> delete(@PathVariable long creditId)
            throws CreditNotFoundException, UnremovableStatusException {
        creditDetailService.deleteById(creditId);
        return ResponseEntity.ok("CreditDetail with id = " + creditId + " has been deleted");
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<CreditDetail>> deleteAll() {
        return ResponseEntity.ok(creditDetailService.deleteAll());
    }

    @PutMapping("/admin/refreshAll")
    public ResponseEntity<String> refreshAll() {
        creditDetailService.refreshAll();
        return ResponseEntity.ok().body("CreditDetails were refreshed");

    }

}
