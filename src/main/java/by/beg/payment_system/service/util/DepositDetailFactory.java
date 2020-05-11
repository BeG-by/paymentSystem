package by.beg.payment_system.service.util;

import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositDetail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class DepositDetailFactory {

    public static DepositDetail createInstance(Deposit deposit, BigDecimal startBalance) {

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime finishDate = startDate.plusDays(deposit.getPeriod());
        BigDecimal returnBalance = calculate(deposit.getPeriod(), deposit.getRate(), startBalance, deposit.isCapitalization());

        return new DepositDetail(startDate, finishDate, startBalance, returnBalance, deposit);

    }


    private static BigDecimal calculate(int period, BigDecimal rate, BigDecimal startMoney, boolean isCapitalization) {

        BigDecimal monthRate = rate.divide(new BigDecimal(1200), new MathContext(4, RoundingMode.CEILING));

        if (isCapitalization) {
            while (period > 0) {
                startMoney = startMoney.add(startMoney.multiply(monthRate));
                period = period - 30;
            }

        } else {
            int month = period / 30;
            startMoney = startMoney.add(startMoney.multiply(monthRate.multiply(BigDecimal.valueOf(month))));
        }


        return startMoney.setScale(2, RoundingMode.CEILING);

    }


}
