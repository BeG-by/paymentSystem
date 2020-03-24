package by.beg.payment_system.util;

import by.beg.payment_system.model.finance.CurrencyType;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositType;

import java.util.Calendar;
import java.util.Date;


public class DepositFactory {

    public static Deposit getInstance(DepositType type) {

        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        Deposit deposit = null;

        switch (type) {
            case USD180:
                calendar.add(Calendar.DATE, 180);
                deposit = new Deposit(DepositType.USD180, CurrencyType.USD, startDate, 180, calendar.getTime(), 2.7, true);
                break;
            case USD90:
                calendar.add(Calendar.DATE, 90);
                deposit = new Deposit(DepositType.USD90, CurrencyType.USD, startDate, 90, calendar.getTime(), 2.1, false);
                break;
            case EUR360:
                calendar.add(Calendar.DATE, 360);
                deposit = new Deposit(DepositType.EUR360, CurrencyType.EUR, startDate, 360, calendar.getTime(), 2.2, true);
                break;
            case RUB360:
                calendar.add(Calendar.DATE, 360);
                deposit = new Deposit(DepositType.RUB360, CurrencyType.RUB, startDate, 360, calendar.getTime(), 6.8, false);
                break;
            case BYN240:
                calendar.add(Calendar.DATE, 240);
                deposit = new Deposit(DepositType.BYN240, CurrencyType.BYN, startDate, 240, calendar.getTime(), 10.3, true);
                break;
            case BYN180:
                calendar.add(Calendar.DATE, 180);
                deposit = new Deposit(DepositType.BYN180, CurrencyType.BYN, startDate, 180, calendar.getTime(), 11.1, true);
        }


        return deposit;

    }


}
