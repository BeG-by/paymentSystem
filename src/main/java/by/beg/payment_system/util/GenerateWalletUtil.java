package by.beg.payment_system.util;

import java.util.Random;

public class GenerateWalletUtil {

    private static Random random = new Random();
    private final static int WALLET_LENGTH = 12;


    public static String generateWalletValue() {

        String result = "";

        for (int i = 0; i < WALLET_LENGTH; i++) {
            int randomDigit = random.nextInt(10);

            if (i == 0 && randomDigit == 0) {
                do {
                    randomDigit = random.nextInt(10);
                } while (randomDigit != 0);
            }

            result += randomDigit;

        }

        return result;
    }

}