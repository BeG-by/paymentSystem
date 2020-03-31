package by.beg.payment_system.controller;

import by.beg.payment_system.exception.*;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.service.MailSenderService;
import by.beg.payment_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailSenderController {

    private MailSenderService mailSenderService;
    private UserService userService;

    public MailSenderController(MailSenderService mailSenderService, UserService userService) {
        this.mailSenderService = mailSenderService;
        this.userService = userService;
    }

    //ADMIN

    @GetMapping("/sendRates/{userId}")
    public ResponseEntity<String> sendRates(@RequestHeader String token, @PathVariable long userId)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException, CurrencyConverterException, UserNotFoundException {

        checkAdminRole(userService.checkAuthorization(token));
        mailSenderService.sendExchangeRates(userId);
        return ResponseEntity.ok("Mail has been sent.");

    }

    @GetMapping("/blockNotification/{userId}")
    public ResponseEntity<String> blockNotification(@RequestHeader String token, @PathVariable long userId)
            throws UserIsNotAuthorizedException, UserBlockedException, NoAccessException, UnremovableStatusException {

        checkAdminRole(userService.checkAuthorization(token));
        mailSenderService.sendBlockNotification(userId);
        return ResponseEntity.ok("Mail has been sent.");

    }

    private void checkAdminRole(User user) throws NoAccessException {
        if (!user.getUserRole().equals(UserRole.ADMIN)) {
            throw new NoAccessException();
        }
    }

}
