package by.beg.payment_system.service;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.service.util.CurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;


@Service
@Transactional
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {

    private JavaMailSender javaMailSender;
    private UserRepository userRepository;
    private CurrencyConverter currencyConverter;

    @Value("${spring.mail.username}")
    private String username;

    public MailSenderServiceImpl(JavaMailSender javaMailSender,
                                 UserRepository userRepository, CurrencyConverter currencyConverter) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public void sendExchangeRates(long userId) throws CurrencyConverterException, UserNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(username);
        message.setSubject("Current exchange rates.");

        String ratesResult = String.format("%-20s%-20s%n", "Currency", "Rate");

        for (Map.Entry<String, BigDecimal> entry : currencyConverter.getAllRates().entrySet()) {
            String format = String.format(
                    "%-15s%-15s%n", entry.getKey(), entry.getValue());
            ratesResult = ratesResult.concat(format);
        }

        message.setText(ratesResult);

        javaMailSender.send(message);
        log.info("Message has been sent to user: " + user);

    }

    @Override
    public void sendBlockNotification(long userId) throws UnremovableStatusException {

        User user = userRepository.findById(userId).
                filter(u -> u.getStatus().equals(Status.BLOCKED)).orElseThrow(UnremovableStatusException::new);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(username);
        message.setSubject("Notification about blocking.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy" , Locale.ENGLISH);
        message.setText("You have been blocked. (" + user.getLastUpdate().format(formatter) + ")");

        javaMailSender.send(message);
        log.info("Message has been sent to user: " + user);

    }

}
