package by.beg.payment_system.controller;

import by.beg.payment_system.dto.request.DateFilterRequestDTO;
import by.beg.payment_system.dto.response.MessageResponseDTO;
import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.LackOfMoneyException;
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
    public ResponseEntity<MessageResponseDTO> transfer(@RequestBody @Valid TransferDetail transferDetail, Principal principal)
            throws LackOfMoneyException, CurrencyConverterException {
        User currentUser = userService.findCurrentUser(principal.getName());
        transferService.makeTransfer(currentUser, transferDetail);

        MessageResponseDTO message = new MessageResponseDTO("Transfer to wallet "
                + transferDetail.getTargetWalletValue() + " has been successfully made");

        return ResponseEntity.ok(message);
    }

    @GetMapping("/findAllExchangeRates")
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

    @DeleteMapping("/admin/deleteById/{transferId}")
    public ResponseEntity<MessageResponseDTO> deleteById(@PathVariable long transferId) {
        transferService.deleteById(transferId);
        MessageResponseDTO message = new MessageResponseDTO("Transfer with id = " + transferId + " was deleted");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteAllBetweenDate")
    public ResponseEntity<List<TransferDetail>> deleteAll(@RequestBody @Valid DateFilterRequestDTO dateDTO) {
        return ResponseEntity.ok(transferService.deleteBetweenDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
    }

}
