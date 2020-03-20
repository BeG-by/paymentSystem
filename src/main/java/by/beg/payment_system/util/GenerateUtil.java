package by.beg.payment_system.util;

import java.util.Random;

public class GenerateUtil {

    private static Random random = new Random();
    private static char[] chars;

    static {
        chars = new char[26];
        int count = 97;
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) count;
            count++;
        }
    }


    public static String generateToken() {
        int randomNum = random.nextInt();
        char[] charTokens = new char[7];

        for (int i = 0; i < charTokens.length; i++) {
            charTokens[i] = chars[random.nextInt(chars.length)];
        }

        String string = new String(charTokens);

        return string + randomNum;

    }

    public static String generateWalletValue() {
        int max = 999_999_999;
        int min = 100_000_000;
        return String.valueOf(random.nextInt(max - min) + min);
    }


}