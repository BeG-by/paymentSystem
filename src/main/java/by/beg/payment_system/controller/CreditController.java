package by.beg.payment_system.controller;

import by.beg.payment_system.exception.CreditIsPresentException;
import by.beg.payment_system.exception.CreditNotFoundException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/credit")
@Validated
public class CreditController {

    private CreditService creditService;

    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    //USER

    @GetMapping("/findAllAvailable")
    public ResponseEntity<List<Credit>> findAllAvailable() {
        return ResponseEntity.ok(creditService.findAllAvailable());
    }


    //ADMIN

    @PostMapping("/admin/create")
    public ResponseEntity<String> create(@RequestBody @Valid Credit credit) throws CreditIsPresentException {
        creditService.create(credit);
        return ResponseEntity.ok("Credit " + credit.getName() + " has been created");
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<Credit>> findAll() {
        return ResponseEntity.ok(creditService.findAll());
    }

    @GetMapping("/admin/findById/{creditId}")
    public ResponseEntity<Credit> findById(@PathVariable long creditId) throws CreditNotFoundException {
        return ResponseEntity.ok(creditService.findById(creditId));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<String> update(@RequestBody @Valid Credit credit) throws CreditNotFoundException {
        creditService.update(credit);
        return ResponseEntity.ok("Credit with id " + credit.getId() + " has been updated");
    }

    @DeleteMapping("/admin/deleteById/{creditId}")
    public ResponseEntity<String> delete(@PathVariable long creditId) throws UnremovableStatusException, CreditNotFoundException {
        creditService.delete(creditId);
        return ResponseEntity.ok("Credit with id " + creditId + " has been deleted");
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<Credit>> deleteAll() {
        return ResponseEntity.ok(creditService.deleteAll());
    }

}
