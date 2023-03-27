package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Gamma;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NonOverlappingTest implements Test {

    // TODO
    private String templatePattern = "00000001";
//    private String template_Pattern = "00000001";
    private String template_Pattern = "001";
//    private int block = 8;
    private int block = 10;

    @Override
    public Map<String, Object> test(String binaryDatas, boolean verbose) {

        // Define the binary data and template pattern
        int[] binaryData = stringToInt(binaryDatas);
        int[] templatePattern = stringToInt(template_Pattern);

        // Define the block size and calculate the number of blocks
        int blockSize = block;
        int numBlocks = binaryData.length / blockSize;

        // Initialize an array to store the number of hits for each block
        int[] patternCounts = new int[numBlocks];
        Arrays.fill(patternCounts, 0);

        // Iterate over the blocks and count the number of hits for the template pattern
        for (int i = 0; i < numBlocks; i++) {
            int blockStart = i * blockSize;
            int blockEnd = blockStart + blockSize;
            int[] blockData = Arrays.copyOfRange(binaryData, blockStart, blockEnd);
            for (int j = 0; j <= blockSize - templatePattern.length; j++) {
                if (Arrays.equals(Arrays.copyOfRange(blockData, j, j + templatePattern.length), templatePattern)) {
                    patternCounts[i]++;
                }
            }
        }

        // Calculate the expected mean and variance
        double expectedMean = (blockSize - templatePattern.length + 1) / Math.pow(2, templatePattern.length);
        double expectedVariance = blockSize * ((1 / Math.pow(2, templatePattern.length)) - ((2 * templatePattern.length - 1)/(Math.pow(2, 2 * templatePattern.length))));

        // Calculate the test statistic and p-value
        double testStatistic = 0.0;
        for (int i = 0; i < numBlocks; i++) {
            testStatistic += Math.pow(patternCounts[i] - expectedMean, 2) / expectedVariance;
        }
        double pValue = Gamma.regularizedGammaQ(numBlocks / 2.0, testStatistic / 2.0);

        // Output the results
//        System.out.println("Binary data: " + Arrays.toString(binaryData));
//        System.out.println("Template pattern: " + Arrays.toString(templatePattern));
//        System.out.println("Block size: " + blockSize);
//        System.out.println("Pattern counts: " + Arrays.toString(patternCounts));
//        System.out.println("Expected mean: " + expectedMean);
//        System.out.println("Expected variance: " + expectedVariance);
//        System.out.println("Test statistic: " + testStatistic);
//        System.out.println("p-value: " + pValue);

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);
        map.put("testName", "6. Тест на совпадение неперекрывающихся шаблонов");

        return map;
    }

    private int[] stringToInt(String str) {
        int[] array = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '1') {
                array[i] = 1;
            } else if (str.charAt(i) == '0') {
                array[i] = 0;
            }
        }
        return array;
    }

    public Map<String, Object> test2(String binaryData, boolean verbose) {
        int lengthOfBinaryData = binaryData.length();
        int patternSize = templatePattern.length();
        int blockSize = (int) Math.floor(lengthOfBinaryData / block);
        int[] patternCounts = new int[block];
        boolean[] binaryDataArray = stringToBooleanArray(binaryData);
        boolean[] templatePatternArray = stringToBooleanArray(templatePattern);

        double mean = 0, variance = 0;
        for (int count = 0; count < block; count++) {
            int block_start = count * blockSize;
            int block_end = block_start + blockSize;
            String block_data = binaryData.substring(block_start, block_end);

            int inner_count = 0;
            while (inner_count < blockSize) {
                String sub_block = block_data.substring(inner_count, inner_count + patternSize);
//                System.out.println(sub_block);
                if (sub_block.equals(templatePattern)) {
                    patternCounts[count]++;
                    inner_count += patternSize;
                    if (inner_count + patternSize > blockSize) {
                        inner_count = inner_count + patternSize;
                    }
                } else {
                    inner_count++;
                }
            }
//
//            // Mean - µ = (M-m+1)/2m
//            mean = (blockSize - patternSize + 1) / Math.pow(2, patternSize);
//            // Variance - σ2 = M((1/pow(2,m)) - ((2m -1)/pow(2, 2m)))
//            variance = blockSize * ((1 / Math.pow(2, patternSize)) - (((2 * patternSize) - 1) / (Math.pow(2, patternSize * 2))));
        }
//
//        double xObs = 0.0;
//        for (int count = 0; count < block; count++) {
//            xObs += Math.pow(patternCounts[count] - mean, 2.0) / variance;
//        }
//
//        double pValue = Gamma.regularizedGammaQ(block / 2.0, xObs / 2.0);
//        if (verbose) {
//            System.out.println("Non-Overlapping Template Test DEBUG BEGIN:");
//            System.out.println("\tLength of input:\t\t" + lengthOfBinaryData);
//            System.out.println("\tValue of Mean (µ):\t\t" + mean);
//            System.out.println("\tValue of Variance(σ):\t" + variance);
//            System.out.println("\tValue of W:\t\t\t\t" + Arrays.toString(patternCounts));
//            System.out.println("\tValue of xObs:\t\t\t" + xObs);
//            System.out.println("\tP-Value:\t\t\t\t" + pValue);
//            System.out.println("DEBUG END.");
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("pValue", pValue);

        return null;
    }

    public Map<String, Object> tessdt(String binaryData, boolean verbose) {
        int lengthOfBinaryData = binaryData.length();
        int patternSize = templatePattern.length();
        int blockSize = (int) Math.floor(lengthOfBinaryData / block);
        int[] patternCounts = new int[block];

        boolean[] binaryDataArray = stringToBooleanArray(binaryData);
        boolean[] templatePatternArray = stringToBooleanArray(templatePattern);

        for (int count = 0; count < block; count++) {
            int blockStart = count * blockSize;
            int blockEnd = blockStart + blockSize;

            int innerCount = 0;
            while (innerCount < blockSize) {
                boolean match = true;
                for (int j = 0; j < patternSize; j++) {
//                    TODO 1.txt index out of bounds exception
                    if (templatePatternArray[j] != binaryDataArray[innerCount + j + blockStart]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    patternCounts[count]++;
                    innerCount += patternSize;
                } else {
                    innerCount += 1;
                }
            }
        }

        double mean = (double) (blockSize - patternSize + 1) / Math.pow(2, patternSize);
        double variance = blockSize * ((1 / Math.pow(2, patternSize)) - (((2 * patternSize) - 1) / (Math.pow(2, patternSize * 2))));

        double xObs = 0;
        for (int count = 0; count < block; count++) {
            xObs += Math.pow(patternCounts[count] - mean, 2.0) / variance;
        }

        double pValue = Gamma.regularizedGammaQ(block / 2.0, xObs / 2.0);
        if (verbose) {
            System.out.println("Non-Overlapping Template Test DEBUG BEGIN:");
            System.out.println("\tLength of input:\t\t" + lengthOfBinaryData);
            System.out.println("\tValue of Mean (µ):\t\t" + mean);
            System.out.println("\tValue of Variance(σ):\t" + variance);
            System.out.println("\tValue of W:\t\t\t\t" + Arrays.toString(patternCounts));
            System.out.println("\tValue of xObs:\t\t\t" + xObs);
            System.out.println("\tP-Value:\t\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);

        return map;
    }

    public boolean[] stringToBooleanArray(String str) {
        boolean[] arr = new boolean[str.length()];
        for (int i = 0; i < str.length(); i++) {
            arr[i] = str.charAt(i) == '1';
        }
        return arr;
    }
}
