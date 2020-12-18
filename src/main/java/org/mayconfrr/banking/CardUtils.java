package org.mayconfrr.banking;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

class CardUtils {
    private CardUtils() {
    }

    private static final Random random = new Random();
    private static final String IIN = "400000";

    static String generateCardNumber() {
        String partialCardNumber;

        partialCardNumber = IIN + IntStream.generate(() -> random.nextInt(10))
            .limit(9)
            .mapToObj(Integer::toString)
            .reduce("", (accID, digit) -> accID + digit);

        return partialCardNumber + getChecksum(partialCardNumber);
    }

    static String generatePIN() {
        return IntStream.generate(() -> random.nextInt(10))
            .limit(4)
            .mapToObj(Integer::toString)
            .reduce("", (pin, digit) -> pin + digit);
    }

    static char getChecksum(String partialCardNumber) {
        int[] digits = partialCardNumber.chars()
            .map(Character::getNumericValue)
            .toArray();

        IntStream.range(0, digits.length)
            .filter(i -> i % 2 == 0)
            .forEach(i -> digits[i] *= 2);

        IntStream.range(0, digits.length)
            .filter(i -> digits[i] > 9)
            .forEach(i -> digits[i] -= 9);

        return String.valueOf((10 - Arrays.stream(digits).sum() % 10) % 10).charAt(0);
    }
}
