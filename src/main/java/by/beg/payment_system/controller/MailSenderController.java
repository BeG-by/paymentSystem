package by.beg.payment_system.controller;

import by.beg.payment_system.dto.response.MessageResponseDTO;
import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.UnremovableStatusException;
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
    public ResponseEntity<MessageResponseDTO> sendRates(@PathVariable long userId) throws CurrencyConverterException {
        mailSenderService.sendExchangeRates(userId);
        MessageResponseDTO message = new MessageResponseDTO("Mail has been sent");
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/blockNotification/{userId}")
    public ResponseEntity<MessageResponseDTO> sendNotification(@PathVariable long userId) throws UnremovableStatusException {
        mailSenderService.sendBlockNotification(userId);
        MessageResponseDTO message = new MessageResponseDTO("Mail has been sent");
        return ResponseEntity.ok(message);
    }

}
