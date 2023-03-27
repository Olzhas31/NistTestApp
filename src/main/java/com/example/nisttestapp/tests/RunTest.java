package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Erf;

import java.util.HashMap;
import java.util.Map;

public class RunTest implements Test {

    @Override
    public Map test(String binaryData, boolean verbose) {
        int oneCount = 0, vObs = 0;
        double pi, tau, pValue;
        int lengthOfBinaryData = binaryData.length();

        tau = 2 / Math.sqrt(lengthOfBinaryData);
        oneCount = binaryData.length() - binaryData.replace("1", "").length();

        pi = (double) oneCount / lengthOfBinaryData;

        if (Math.abs(pi - 0.5) >= tau) {
            System.out.println("The test should not have been run because of a failure to pass test 1, the Frequency (Monobit) test.");
//            TODO return false
            return null;
        } else {
            for (int i = 1; i < lengthOfBinaryData; i++) {
                if (binaryData.charAt(i) != binaryData.charAt(i - 1)) {
                    vObs++;
                }
            }
            vObs++;
            pValue = Erf.erfc(Math.abs(vObs - (2 * (lengthOfBinaryData) * pi * (1 - pi))) / (2 * Math.sqrt(2 * lengthOfBinaryData) * pi * (1 - pi)));
        }

        if (verbose) {
            System.out.println("Run Test DEBUG BEGIN:");
            System.out.println("\tLength of input:\t\t\t\t" + lengthOfBinaryData);
            System.out.println("\tTau (2/sqrt(length of input)):\t" + tau);
            System.out.println("\t# of '1':\t\t\t\t\t\t" + oneCount);
            System.out.println("\t# of '0':\t\t\t\t\t\t" + (lengthOfBinaryData - oneCount));
            System.out.println("\tPI (1 count / length of input):\t" + pi);
            System.out.println("\tvObs:\t\t\t\t\t\t\t" + vObs);
            System.out.println("\tP-Value:\t\t\t\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);
        map.put("testName", "3. Тест на последовательность одинаковых битов");

        return map;
    }

}
