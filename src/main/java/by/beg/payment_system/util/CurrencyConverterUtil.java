package by.beg.payment_system.util;

import by.beg.payment_system.model.finance.CurrencyType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Data
@ConfigurationProperties("converter")
public class CurrencyConverterUtil {

    private Map<String, Double> exchangeRates;
    private final static String DELIMITER = "to";

    public BigDecimal convertMoney(CurrencyType rootCurrencyType, CurrencyType targetCurrencyType, BigDecimal transferMoney) {

        if (rootCurrencyType.equals(targetCurrencyType)) {
            return transferMoney;
        }

        String key = rootCurrencyType.toString() + DELIMITER + targetCurrencyType.toString();
        return transferMoney.multiply(BigDecimal.valueOf(exchangeRates.get(key)));

    }
}
