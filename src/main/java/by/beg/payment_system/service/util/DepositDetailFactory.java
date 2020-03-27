package by.beg.payment_system.service.util;

import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositDetail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;


public class DepositDetailFactory {

    public static DepositDetail getInstance(Deposit deposit, BigDecimal startBalance) {

        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, deposit.getPeriod());
        BigDecimal returnBalance = calculate(deposit.getPeriod(), deposit.getRate(), startBalance, deposit.isCapitalization());

        return new DepositDetail(startDate, calendar.getTime(), startBalance, returnBalance, deposit);

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
