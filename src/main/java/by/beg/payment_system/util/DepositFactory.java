package by.beg.payment_system.util;

import by.beg.payment_system.model.CurrencyType;
import by.beg.payment_system.model.Deposit;
import by.beg.payment_system.model.DepositType;

import java.util.Calendar;
import java.util.Date;


public class DepositFactory {

    public static Deposit getInstance(DepositType type) {

        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        switch (type) {
            case USD180:
                calendar.add(Calendar.DATE, 180);
                return new Deposit(DepositType.USD180, CurrencyType.USD, startDate, 180, calendar.getTime(), 2.7, false);
            case EUR360:
                calendar.add(Calendar.DATE, 360);
                return new Deposit(DepositType.EUR360, CurrencyType.EUR, startDate, 360, calendar.getTime(), 2.2, false);
            case RUB360:
                calendar.add(Calendar.DATE, 360);
                return new Deposit(DepositType.RUB360, CurrencyType.RUB, startDate, 360, calendar.getTime(), 6.8, false);
            case BYN180:
                calendar.add(Calendar.DATE, 180);
                return new Deposit(DepositType.BYN180, CurrencyType.BYN, startDate, 180, calendar.getTime(), 11.1, false);
        }

        return null;

    }

}
