package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Gamma;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.math3.util.FastMath.max;

public class LongestOneBlockTest implements Test {

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        int lengthOfBinaryData = binaryData.length();
        int k, m;
        double[] vValues, piValues;

        Map<String, Object> map = new HashMap<>();
        map.put("testName", "4. Тест на самую длинную последовательность единиц в блоке");

        if (lengthOfBinaryData < 128) {
            // TODO
            map.put("pValue", 0.0);
            map.put("error", "Error: Not enough data to run this test");
            return map;
        } else if (lengthOfBinaryData < 6272) {
            k = 3;
            m = 8;
            vValues = new double[]{1, 2, 3, 4};
            piValues = new double[]{0.2148, 0.3672, 0.2305, 0.1875};
        } else if (lengthOfBinaryData < 750000) {
            k = 5;
            m = 128;
            vValues = new double[]{4, 5, 6, 7, 8, 9};
            piValues = new double[]{0.1174, 0.2430, 0.2493, 0.1752, 0.1027, 0.1124};
        } else {
            k = 6;
            m = 10000;
            vValues = new double[]{10, 11, 12, 13, 14, 15, 16};
            piValues = new double[]{0.0882, 0.2092, 0.2483, 0.1933, 0.1208, 0.0675, 0.0727};
        }

        int numberOfBlocks = (int) Math.floor(lengthOfBinaryData / m);
        int blockStart = 0;
        int blockEnd = m;
        double xObs = 0.0;
        double[] frequencies = new double[k + 1];

        for (int count = 0; count < numberOfBlocks; count++) {
            String blockData = binaryData.substring(blockStart, blockEnd);
            int maxRunCount = 0;
            int runCount = 0;
            for (int i = 0; i < blockData.length(); i++) {
                char bit = blockData.charAt(i);
                if (bit == '1') {
                    runCount += 1;
                    maxRunCount = Math.max(maxRunCount, runCount);
                } else {
                    maxRunCount = Math.max(maxRunCount, runCount);
                    runCount = 0;
                }
            }

            max(maxRunCount, runCount);

            if (maxRunCount < vValues[0]) {
                frequencies[0] += 1;
            }
            for (int j = 0; j < k; j++) {
                if (maxRunCount == vValues[j]) {
                    frequencies[j] += 1;
                }
            }
            if (maxRunCount > vValues[k - 1]) {
                frequencies[k] += 1;
            }
            blockStart += m;
            blockEnd += m;
        }

        for (int count = 0; count < frequencies.length; count++) {
            xObs += Math.pow((frequencies[count] - (numberOfBlocks * piValues[count])), 2.0) / (
                    numberOfBlocks * piValues[count]);
        }

        double pValue = Gamma.regularizedGammaQ(k / 2.0, xObs / 2.0);

        if (verbose) {
            System.out.println("Run Test (Longest Run of Ones in a Block) DEBUG BEGIN:");
            System.out.println("\tLength of input:\t\t\t\t" + lengthOfBinaryData);
            System.out.println("\tSize of each Block:\t\t\t\t" + m);
            System.out.println("\tNumber of Block:\t\t\t\t" + numberOfBlocks);
            System.out.println("\tValue of K:\t\t\t\t\t\t" + k);
            System.out.println("\tValue of PIs:\t\t\t\t\t" + Arrays.toString(piValues));
            System.out.println("\tFrequencies:\t\t\t\t\t" + Arrays.toString(frequencies));
            System.out.println("\txObs:\t\t\t\t\t\t\t" + xObs);
            System.out.println("\tP-Value:\t\t\t\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        map.put("pValue", pValue);

        return map;
    }
}
