package by.beg.payment_system.service.util;

import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.model.finance.CreditDetail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class CreditDetailFactory {

    public static CreditDetail getInstance(Credit credit, BigDecimal startDebt) {

        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, credit.getPeriod());
        BigDecimal fullDebt = calculate(credit.getPeriod(), credit.getRate(), startDebt);

        return new CreditDetail(startDate, calendar.getTime(), startDate, startDebt, fullDebt, fullDebt, credit);

    }

    private static BigDecimal calculate(int period, BigDecimal rate, BigDecimal debt) {
        BigDecimal monthRate = rate.divide(new BigDecimal(1200), new MathContext(4, RoundingMode.CEILING));
        int month = period / 30;
        debt = debt.add(debt.multiply(monthRate.multiply(BigDecimal.valueOf(month))));
        return debt.setScale(2, RoundingMode.CEILING);
    }

}
