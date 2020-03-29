package by.beg.payment_system;

import by.beg.payment_system.service.util.CurrencyConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(CurrencyConverter.class)
@EnableScheduling
public class PaymentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentSystemApplication.class, args);
    }

}
