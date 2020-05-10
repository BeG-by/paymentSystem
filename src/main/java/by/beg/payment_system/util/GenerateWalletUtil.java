package by.beg.payment_system.util;

import java.util.Random;

public class GenerateWalletUtil {

    private static Random random = new Random();
    private static char[] digits;
    public final static int WALLET_LENGTH = 12;

    static {
        digits = new char[9];
        int count = '0';
        for (int i = 0; i < digits.length; i++) {
            digits[i] = (char) count;
            count++;
        }
    }


    public static String generateWalletValue() {

        String result = "";

        for (int i = 0; i < WALLET_LENGTH; i++) {
            int randomDigit = random.nextInt(digits.length);

            if (i == 0 && randomDigit == 0) {
                do {
                    randomDigit = random.nextInt(digits.length);
                } while (randomDigit != 0);
            }

            result += randomDigit;

        }

        return result;
    }

}