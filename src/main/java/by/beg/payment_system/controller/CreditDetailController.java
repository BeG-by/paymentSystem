package by.beg.payment_system.controller;

import by.beg.payment_system.dto.request.CreditOpenRequestDTO;
import by.beg.payment_system.dto.request.DateFilterRequestDTO;
import by.beg.payment_system.dto.response.MessageResponseDTO;
import by.beg.payment_system.exception.LackOfMoneyException;
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
    public ResponseEntity<MessageResponseDTO> create(@RequestBody @Valid CreditOpenRequestDTO openDTO, Principal principal) {
        User currentUser = userService.findCurrentUser(principal.getName());
        creditDetailService.create(openDTO, currentUser);
        MessageResponseDTO message = new MessageResponseDTO("CreditDetail has been successfully create");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/find")
    public ResponseEntity<CreditDetail> find(Principal principal) {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(creditDetailService.findByUser(currentUser));
    }

    @PostMapping("/repay")
    public ResponseEntity<CreditDetail> repay(@RequestBody @Valid CreditOpenRequestDTO openDTO, Principal principal)
            throws LackOfMoneyException {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(creditDetailService.repayDebt(currentUser, openDTO));
    }

    //ADMIN

    @GetMapping("/admin/findById/{userId}")
    public ResponseEntity<CreditDetail> findById(@PathVariable long userId) {
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
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable long creditId) {
        creditDetailService.deleteById(creditId);
        MessageResponseDTO message = new MessageResponseDTO("CreditDetail has been successfully create");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<CreditDetail>> deleteAll() {
        return ResponseEntity.ok(creditDetailService.deleteAll());
    }

    @PutMapping("/admin/refreshAll")
    public ResponseEntity<MessageResponseDTO> refreshAll() {
        creditDetailService.refreshAll();
        MessageResponseDTO message = new MessageResponseDTO("CreditDetails were refreshed");
        return ResponseEntity.ok(message);

    }

}
