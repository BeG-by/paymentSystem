package by.beg.payment_system.service.util;

import by.beg.payment_system.model.finance.Deposit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class DepositCalculate {

    public static BigDecimal calculate(Deposit deposit) {

        BigDecimal resultMoney = deposit.getBalance();
        BigDecimal monthRate = BigDecimal.valueOf(deposit.getRate()).divide(new BigDecimal(1200), new MathContext(4, RoundingMode.CEILING));

        int days = deposit.getDays();

        if (deposit.isCapitalization()) {
            while (days > 0) {
                resultMoney = resultMoney.add(resultMoney.multiply(monthRate));
                days = days - 30;
            }

        } else {
            int month = days / 30;
            resultMoney = resultMoney.add(resultMoney.multiply(monthRate.multiply(BigDecimal.valueOf(month))));
        }


        return resultMoney.setScale(2 , RoundingMode.CEILING);

    }
}
