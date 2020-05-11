package by.beg.payment_system.service.util;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
@ConfigurationProperties("converter")
public class CurrencyConverter {

    private Map<String, String> exchangeRatesURL;
    private final static String DELIMITER = "to";

    public BigDecimal convertMoney(CurrencyType rootCurrencyType, CurrencyType targetCurrencyType, BigDecimal transferMoney) throws CurrencyConverterException {

        if (rootCurrencyType.equals(targetCurrencyType)) {
            return transferMoney;
        }

        String key = rootCurrencyType.toString() + DELIMITER + targetCurrencyType.toString();
        return transferMoney.multiply(parseRate(exchangeRatesURL.get(key)));

    }

    public Map<String, BigDecimal> receiveAllRates() throws CurrencyConverterException {

        Map<String, BigDecimal> rates = new HashMap<>();

        for (Map.Entry<String, String> entry : exchangeRatesURL.entrySet()) {
            rates.put(entry.getKey(), parseRate(entry.getValue()));
        }

        return rates;

    }

    private BigDecimal parseRate(String url) throws CurrencyConverterException {

        String result;

        try {
            Document page = Jsoup.parse(new URL(url), 3000);
            result = page.select("span[class=converter-100__info-currency-bold]").text().split("\\s")[0];
        } catch (IOException e) {
            throw new CurrencyConverterException();
        }

        return new BigDecimal(result, new MathContext(5, RoundingMode.CEILING));

    }


}
