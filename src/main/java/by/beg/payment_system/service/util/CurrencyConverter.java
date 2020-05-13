package by.beg.payment_system.service.util;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
@Slf4j
public class CurrencyConverter {

    private Map<String, String> exchangeRatesUrl = new HashMap<>();

    @Value("${converter.rates.url}")
    private String url;
    private final static String DELIMITER = "-";
    private final static int QUANTITY = 1;

    @PostConstruct
    private void init() {
        CurrencyType[] currencyTypes = CurrencyType.values();

        for (int i = 0; i < currencyTypes.length; i++) {
            for (int j = 0; j < currencyTypes.length; j++) {
                if (i != j) {
                    String currency1 = currencyTypes[i].toString();
                    String currency2 = currencyTypes[j].toString();
                    exchangeRatesUrl.put(currency1 + DELIMITER + currency2,
                            url + currency1.toLowerCase() + DELIMITER + currency2.toLowerCase() + "/" + QUANTITY);
                }
            }
        }

        log.info("Exchange rates were loaded: " + exchangeRatesUrl);
    }

    public BigDecimal convertMoney(CurrencyType rootCurrencyType, CurrencyType targetCurrencyType, BigDecimal transferMoney)
            throws CurrencyConverterException {

        if (rootCurrencyType.equals(targetCurrencyType)) {
            return transferMoney;
        }

        String key = rootCurrencyType.toString() + DELIMITER + targetCurrencyType.toString();
        return transferMoney.multiply(parseRate(exchangeRatesUrl.get(key)));

    }

    public Map<String, BigDecimal> receiveAllRates() throws CurrencyConverterException {

        Map<String, BigDecimal> rates = new HashMap<>();

        for (Map.Entry<String, String> entry : exchangeRatesUrl.entrySet()) {
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
