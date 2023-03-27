package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Gamma;

import java.util.HashMap;
import java.util.Map;

public class ApproximateEntropyTest implements Test {

    // TODO по умолчанию 10
    private int patternLength = 10;
//    private int patternLength = 3;

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        int lengthOfBinaryData = binaryData.length();
        binaryData += binaryData.substring(0, patternLength + 1);
        String maxPattern = "";
        for (int i = 0; i < patternLength + 2; i++) {
            maxPattern += "1";
        }

        double[] vobs01 = new double[(int)Long.parseLong(maxPattern.substring(0, patternLength), 2) + 1];
        double[] vobs02 = new double[(int)Long.parseLong(maxPattern.substring(0, patternLength + 1), 2) + 1];

        for (int i = 0; i < lengthOfBinaryData; i++) {
            vobs01[(int)Long.parseLong(binaryData.substring(i, i + patternLength), 2)] += 1;
            vobs02[(int)Long.parseLong(binaryData.substring(i, i + patternLength + 1), 2)] += 1;
        }

        double[][] vobs = {vobs01, vobs02};

        double[] sums = new double[2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < vobs[i].length; j++) {
                if (vobs[i][j] > 0) {
                    sums[i] += vobs[i][j] * Math.log(vobs[i][j] / lengthOfBinaryData);
                }
            }
            sums[i] /= lengthOfBinaryData;
        }
        double ape = sums[0] - sums[1];

        double xObs = 2.0 * lengthOfBinaryData * (Math.log(2) - ape);

        double pValue = Gamma.regularizedGammaQ(Math.pow(2, patternLength - 1), xObs / 2.0);

        if (verbose) {
            System.out.println("Approximate Entropy Test DEBUG BEGIN:");
            System.out.println("\tLength of input:\t\t\t" + lengthOfBinaryData);
            System.out.println("\tLength of each block:\t\t" + patternLength);
            System.out.println("\tApEn(m):\t\t\t\t\t" + ape);
            System.out.println("\txObs:\t\t\t\t\t\t" + xObs);
            System.out.println("\tP-Value:\t\t\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);
        map.put("testName", "9. Тест приблизительной энтропии");

        return map;
    }
}
