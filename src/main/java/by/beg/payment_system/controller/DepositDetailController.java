package by.beg.payment_system.controller;

import by.beg.payment_system.dto.request.DateFilterRequestDTO;
import by.beg.payment_system.dto.request.DepositOpenRequestDTO;
import by.beg.payment_system.dto.response.MessageResponseDTO;
import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.UnremovableStatusException;
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
    public ResponseEntity<MessageResponseDTO> createDeposit(@RequestBody @Valid DepositOpenRequestDTO openDTO, Principal principal)
            throws LackOfMoneyException, CurrencyConverterException {
        User currentUser = userService.findCurrentUser(principal.getName());
        depositDetailService.create(openDTO, currentUser);
        MessageResponseDTO message = new MessageResponseDTO("DepositDetail has been successfully create");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DepositDetail>> findAllByUser(Principal principal) {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(depositDetailService.findAllByUser(currentUser));
    }

    @PutMapping("/pickUp")
    public ResponseEntity<List<DepositDetail>> pickUpDeposits(Principal principal) {
        User currentUser = userService.findCurrentUser(principal.getName());
        return ResponseEntity.ok(depositDetailService.pickUp(currentUser));
    }


    //ADMIN

    @GetMapping("/admin/findAll/{userId}")
    public ResponseEntity<List<DepositDetail>> findAllById(@PathVariable long userId) {
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
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable long depositId) throws UnremovableStatusException {
        depositDetailService.deleteById(depositId);
        MessageResponseDTO message = new MessageResponseDTO("DepositDetail with id = " + depositId + " has been deleted");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<DepositDetail>> deleteAll() {
        return ResponseEntity.ok(depositDetailService.deleteAll());
    }

    @PutMapping("/admin/refreshAll")
    public ResponseEntity<MessageResponseDTO> refreshAll() {
        depositDetailService.refreshAll();
        MessageResponseDTO message = new MessageResponseDTO("DepositsDetails have been refreshed");
        return ResponseEntity.ok(message);
    }

}
