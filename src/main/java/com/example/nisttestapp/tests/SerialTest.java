package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Gamma;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SerialTest implements Test {

//    TODO 16
    private int patternLength = 16;
//    private int patternLength = 3;

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        verbose = true;
        int lengthOfBinaryData = binaryData.length();
        binaryData += binaryData.substring(0, patternLength - 1);

        String maxPattern = "";
        for (int i = 0; i < patternLength + 1; i++) {
            maxPattern += "1";
        }

        double[] vobs01 = new double[(int) Long.parseLong(maxPattern.substring(0, patternLength), 2) + 1];
        double[] vobs02 = new double[(int) Long.parseLong(maxPattern.substring(0, patternLength - 1), 2) + 1];
        double[] vobs03 = new double[(int) Long.parseLong(maxPattern.substring(0, patternLength - 2), 2) + 1];

        for (int i = 0; i < lengthOfBinaryData; i++) {
            vobs01[(int) Long.parseLong(binaryData.substring(i, i + patternLength), 2)] += 1;
            vobs02[(int) Long.parseLong(binaryData.substring(i, i + patternLength - 1), 2)] += 1;
            vobs03[(int) Long.parseLong(binaryData.substring(i, i + patternLength - 2), 2)] += 1;
        }

        double[][] vobs = {vobs01, vobs02, vobs03};

        double[] sums = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < vobs[i].length; j++) {
                sums[i] += Math.pow(vobs[i][j], 2);
            }
            sums[i] = (sums[i] * Math.pow(2, patternLength - i) / lengthOfBinaryData) - lengthOfBinaryData;
        }

        double nabla01 = sums[0] - sums[1];
        double nabla02 = sums[0] - 2.0 * sums[1] + sums[2];

        double pValue01 = Gamma.regularizedGammaQ(Math.pow(2, patternLength - 1) / 2, nabla01 / 2.0);
        double pValue02 = Gamma.regularizedGammaQ(Math.pow(2, patternLength - 2) / 2, nabla02 / 2.0);

        if (verbose) {
            System.out.println("Serial Test DEBUG BEGIN:");
            System.out.println("\tLength of input:\t" + lengthOfBinaryData);
            System.out.println("\tValue of Sai:\t\t" + Arrays.toString(sums));
            System.out.println("\tValue of Nabla:\t\t" + nabla01 + ", " + nabla02);
            System.out.println("\tP-Value 01:\t\t\t" + pValue01);
            System.out.println("\tP-Value 02:\t\t\t" + pValue02);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue01);
        map.put("pValue2", pValue02);
        map.put("testName", "8. Тест на периодичность");

        return map;
    }
}
