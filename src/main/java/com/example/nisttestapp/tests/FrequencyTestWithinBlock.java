package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Gamma;

import java.util.HashMap;
import java.util.Map;

public class FrequencyTestWithinBlock implements Test {

    // TODO 128 настройкадан беретіндей жасаса болады
//    private int blockSize = 128;
//    private int blockSize = 3;
    private int blockSize = 10;

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        int lengthOfBitString = binaryData.length();
        if (lengthOfBitString < blockSize) {
            blockSize = lengthOfBitString;
        }

        int numberOfBlocks = lengthOfBitString / blockSize;

        if (numberOfBlocks == 1) {
            // TODO
//            return monobitTest(binaryData.substring(0, blockSize), verbose);
        }

        int blockStart = 0;
        int blockEnd = blockSize;
        double proportionSum = 0.0;

        for (int i = 0; i < numberOfBlocks; i++) {
            String blockData = binaryData.substring(blockStart, blockEnd);
            int oneCount = 0;
            for (int j = 0; j < blockSize; j++) {
                if (blockData.charAt(j) == '1') {
                    oneCount++;
                }
            }
            double pi = (double) oneCount / blockSize;
            proportionSum += Math.pow(pi - 0.5, 2);
            blockStart += blockSize;
            blockEnd += blockSize;
        }

        double result = 4.0 * blockSize * proportionSum;
        double pValue = Gamma.regularizedGammaQ(numberOfBlocks / 2.0, result / 2.0);

        if (verbose) {
            System.out.println("Frequency Test (Block Frequency Test) DEBUG BEGIN:");
            System.out.println("\tLength of input:\t" + lengthOfBitString);
            System.out.println("\tSize of Block:\t\t" + blockSize);
            System.out.println("\tNumber of Blocks:\t" + numberOfBlocks);
            System.out.println("\tCHI Squared:\t\t" + result);
            System.out.println("\t1st:\t\t\t\t" + numberOfBlocks / 2.0);
            System.out.println("\t2nd:\t\t\t\t" + result / 2.0);
            System.out.println("\tP-Value:\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);
        map.put("testName", "2. Частотный блочный тест.");

        return map;
    }
}
