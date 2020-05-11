package by.beg.payment_system.controller;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailSenderController {

    private MailSenderService mailSenderService;

    @Autowired
    public MailSenderController(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    //ADMIN

    @GetMapping("/admin/sendRates/{userId}")
    public ResponseEntity<String> sendRates(@PathVariable long userId) throws CurrencyConverterException, UserNotFoundException {
        mailSenderService.sendExchangeRates(userId);
        return ResponseEntity.ok("Mail has been sent.");
    }

    @GetMapping("/admin/blockNotification/{userId}")
    public ResponseEntity<String> sendNotification(@PathVariable long userId) throws UnremovableStatusException {
        mailSenderService.sendBlockNotification(userId);
        return ResponseEntity.ok("Mail has been sent.");
    }

}
