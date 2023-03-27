package com.example.nisttestapp.tmp;

import org.apache.commons.math3.special.Gamma;

import java.util.Arrays;

public class TemplateMatching {

    public static boolean overlapping_patterns(String binaryData, boolean verbose, int patternSize, int blockSize) {
        int[] binaryArray = new int[binaryData.length()];
        for (int i = 0; i < binaryData.length(); i++) {
            binaryArray[i] = Character.getNumericValue(binaryData.charAt(i));
        }

        int[] pattern = new int[patternSize];
        for (int i = 0; i < patternSize; i++) {
            pattern[i] = 1;
        }

        int numberOfBlocks = binaryData.length() / blockSize;

        double lambdaVal = (double) (blockSize - patternSize + 1) / Math.pow(2, patternSize);
        double eta = lambdaVal / 2.0;

        double[] pi = new double[6];
        for (int i = 0; i < 5; i++) {
            pi[i] = getProb(i, eta);
        }
        double diff = (double) Arrays.stream(pi).sum();
        pi[5] = 1.0 - diff;

        int[] patternCounts = new int[6];
        for (int i = 0; i < numberOfBlocks; i++) {
            int block_start = i * blockSize;
            int block_end = block_start + blockSize;
            String block_data = binaryData.substring(block_start, block_end);

            int pattern_count = 0;
            int j = 0;
            while (j < blockSize) {
                String sub_block = block_data.substring(j, j + patternSize);
                if (sub_block.equals(pattern.toString())) {
                    pattern_count += 1;
                }
                j += 1;
            }
            if (pattern_count <= 4) {
                patternCounts[pattern_count] += 1;
            }
            else {
                patternCounts[5] += 1;
            }
        }

        double xObs = 0.0;
        for (int i = 0; i < 6; i++) {
            xObs += Math.pow(patternCounts[i] - numberOfBlocks * pi[i], 2) / (numberOfBlocks * pi[i]);
        }
        double pValue = Gamma.regularizedGammaQ(5.0 / 2.0, xObs / 2.0);

        if (verbose) {
            System.out.println("Overlapping Template Test DEBUG BEGIN:");
            System.out.println("\tLength of input:\t\t" + binaryData.length());
            System.out.println("\tValue of Vs:\t\t\t" + Arrays.toString(patternCounts));
            System.out.println("\tValue of xObs:\t\t\t" + xObs);
            System.out.println("\tP-Value:\t\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        return pValue >= 0.01;
    }

    public static double getProb(int u, double x) {
        double out = Math.exp(-x);
        if (u != 0) {
            out = 1.0 * x * Math.exp(2 * -x) * (Math.pow(2, -u)) *
                    hyp1f1(u +1 , 2, x);
        }
        return out;
    }

    private static double hyp1f1(double a, double b, double x) {
        double result = 1.0;
        double term = 1.0;

        for (int k = 1; k <= 100; k++) {
            term *= x / k * (a + k - 1) / (b + k - 1);
            result += term;

            if (Math.abs(term) < 1e-8 * Math.abs(result)) {
                break;
            }
        }
        return result;
    }
}

