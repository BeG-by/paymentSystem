package by.beg.payment_system.util;

import java.util.Random;

public class GenerateTokenUtil {

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


}