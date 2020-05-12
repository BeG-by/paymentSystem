package by.beg.payment_system.controller;

import by.beg.payment_system.dto.response.MessageResponseDTO;
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
    public ResponseEntity<MessageResponseDTO> create(@RequestBody @Valid Credit credit) {
        creditService.create(credit);
        MessageResponseDTO message = new MessageResponseDTO("Credit " + credit.getName() + " has been created");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/findAll")
    public ResponseEntity<List<Credit>> findAll() {
        return ResponseEntity.ok(creditService.findAll());
    }

    @GetMapping("/admin/findById/{creditId}")
    public ResponseEntity<Credit> findById(@PathVariable long creditId) {
        return ResponseEntity.ok(creditService.findById(creditId));
    }

    @PutMapping("/admin/update")
    public ResponseEntity<MessageResponseDTO> update(@RequestBody @Valid Credit credit) {
        creditService.update(credit);
        MessageResponseDTO message = new MessageResponseDTO("Credit with id " + credit.getId() + " has been updated");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteById/{creditId}")
    public ResponseEntity<MessageResponseDTO> delete(@PathVariable long creditId) throws UnremovableStatusException {
        creditService.delete(creditId);
        MessageResponseDTO message = new MessageResponseDTO("Credit with id " + creditId + " has been deleted");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/admin/deleteAll")
    public ResponseEntity<List<Credit>> deleteAll() {
        return ResponseEntity.ok(creditService.deleteAll());
    }

}
