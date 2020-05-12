package by.beg.payment_system.controller;

import by.beg.payment_system.dto.response.MessageResponseDTO;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/deposit")
@Validated
public class DepositController {

    private DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    //USER

    @GetMapping("/findAllAvailable")
    public ResponseEntity<List<Deposit>> getAllAvailable() {
        return ResponseEntity.ok(depositService.findAllAvailable());
    }


    //ADMIN

    @PostMapping("/admin/create")
    public ResponseEntity<MessageResponseDTO> create(@RequestBody @Valid Deposit deposit) {
        depositService.create(deposit);
        MessageResponseDTO message = new MessageResponseDTO("Deposit " + deposit.getName() + " has been created");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<Deposit>> findAll() {
        return ResponseEntity.ok(depositService.findAll());
    }

    @GetMapping("/admin/findById/{depositId}")
    public ResponseEntity<Deposit> findById(@PathVariable long depositId) {
        return ResponseEntity.ok(depositService.findById(depositId));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<MessageResponseDTO> update(@RequestBody @Valid Deposit deposit) {
        depositService.update(deposit);
        MessageResponseDTO message = new MessageResponseDTO("Deposit with id = " + deposit.getId() + " has been updated");
        return ResponseEntity.ok(message);
    }


    @DeleteMapping("/admin/deleteById/{depositId}")
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable long depositId) {
        depositService.delete(depositId);
        MessageResponseDTO message = new MessageResponseDTO("Deposit with id = " + depositId + " has been deleted");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<Deposit>> deleteAll() {
        return ResponseEntity.ok(depositService.deleteAll());
    }

}
