package by.beg.payment_system.controller;

import by.beg.payment_system.exception.DepositIsPresentException;
import by.beg.payment_system.exception.DepositNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
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
    public ResponseEntity<String> create(@RequestBody @Valid Deposit deposit) throws DepositIsPresentException {
        depositService.create(deposit);
        return ResponseEntity.ok("Deposit " + deposit.getName() + " has been created");
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<Deposit>> findAll() {
        return ResponseEntity.ok(depositService.findAll());
    }

    @GetMapping("/admin/findById/{depositId}")
    public ResponseEntity<Deposit> findById(@PathVariable long depositId) throws DepositNotFoundException {
        return ResponseEntity.ok(depositService.findById(depositId));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<String> update(@RequestBody @Valid Deposit deposit) throws DepositNotFoundException {
        depositService.update(deposit);
        return ResponseEntity.ok("Deposit with id = " + deposit.getId() + " has been updated");
    }


    @DeleteMapping("/admin/delete/{depositId}")
    public ResponseEntity<String> delete(@PathVariable long depositId) throws DepositNotFoundException, UnremovableStatusException {
        depositService.delete(depositId);
        return ResponseEntity.ok("Deposit with id = " + depositId + "");
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<Deposit>> deleteAll() {
        return ResponseEntity.ok(depositService.deleteAll());
    }

}
