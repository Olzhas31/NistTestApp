package com.example.nisttestapp.util;
import java.math.BigDecimal;

public class EBinaryExpansion {
    public static void main(String[] args) {
        BigDecimal e = BigDecimal.valueOf(Math.E);
        BigDecimal two = BigDecimal.valueOf(2);
        int precision = 100_000;

        // Set the precision to 100,000 decimal places
        e = e.setScale(precision, BigDecimal.ROUND_HALF_UP);

        // Convert the decimal expansion of e to binary
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < precision; i++) {
            e = e.multiply(two);
            int digit = e.intValue();
            binary.append(digit);
            e = e.subtract(BigDecimal.valueOf(digit));
        }

        System.out.println(binary.toString());
    }
}

