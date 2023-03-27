package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;

import java.util.*;

public class CumulativeSumsTest implements Test {

    private int mode;

    public CumulativeSumsTest(int mode) {
        this.mode = mode;
    }

    public static void main(String[] args) {
        Test test = new CumulativeSumsTest(0);
        test.test("111000111000110011001100110001100000", true);
    }

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        int lengthOfBinaryData = binaryData.length();
        double[] counts = new double[lengthOfBinaryData];

        if (mode != 0) {
            binaryData = new StringBuilder(binaryData).reverse().toString();
        }

        int counter = 0;
        for (char c : binaryData.toCharArray()) {
            int sub = 1;
            if (c == '0') {
                sub = -1;
            }
            if (counter > 0) {
                counts[counter] = counts[counter - 1] + sub;
            } else {
                counts[counter] = sub;
            }

            counter += 1;
        }

        double absMax = findMax(counts);

        int start = (int) Math.floor(0.25 * ((-lengthOfBinaryData / absMax) + 1));
        int end = (int)  Math.floor(0.25 * ((lengthOfBinaryData * 1.0 / absMax) - 1));

        List<Double> termsOne = new ArrayList<Double>();
        for (int k = start; k <= end; k++) {
            double sub = standardNormalCDF2((4 * k - 1) * absMax / Math.sqrt(lengthOfBinaryData));
            termsOne.add(standardNormalCDF2((4 * k + 1) * absMax / Math.sqrt(lengthOfBinaryData)) - sub);
        }

        start = (int) Math.floor(0.25 * ((-lengthOfBinaryData / absMax) - 3));
        end = (int)  Math.floor(0.25 * ((lengthOfBinaryData * 1.0 / absMax) - 1));

        List<Double> termsTwo = new ArrayList<>();
        for (int k = start; k <= end; k++) {
            double sub = standardNormalCDF2((4 * k + 1) * absMax / Math.sqrt(lengthOfBinaryData));
            termsTwo.add(standardNormalCDF2((4 * k + 3) * absMax / Math.sqrt(lengthOfBinaryData)) - sub);
        }

        double pValue = 1.0 - sum(termsOne);
        pValue += sum(termsTwo);

        if (verbose) {
            System.out.println("Cumulative Sums Test DEBUG BEGIN:");
            System.out.println("\tLength of input:\t" + lengthOfBinaryData);
            System.out.println("\tMode:\t\t\t\t" + mode);
            System.out.println("\tValue of z:\t\t\t" + absMax);
            System.out.println("\tP-Value:\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        if (mode == 0) {
            map.put("testName", "10. Тест кумулятивных сумм (Forward)");
        } else if (mode == 1) {
            map.put("testName", "11. Тест кумулятивных сумм (Backward)");
        }
        map.put("pValue", pValue);

        return map;
    }

    private double findMax(double[] counts) {
        double max = 0;
        for (double x: counts) {
            if (Math.abs(x) > max) {
                max = Math.abs(x);
            }
        }
        return max;
    }

    private double sum(List<Double> arr) {
        double sum = 0;
        for (double x : arr) {
            sum += x;
        }
        return sum;
    }

    public static double standardNormalCDF2(double z) {
        double t = 1.0 / (1.0 + 0.2316419 * Math.abs(z));
        double d = 0.3989423 * Math.exp(-z * z / 2.0);
        double p = d * t * (0.319381530 + t * (-0.356563782 + t * (1.781477937 + t * (-1.821255978 + 1.330274429 * t))));
        if (z > 0.0) {
            return 1.0 - p;
        } else {
            return p;
        }
    }
}
