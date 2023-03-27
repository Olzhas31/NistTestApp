package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Erf;

import java.util.HashMap;
import java.util.Map;


public class FrequencyTest implements Test {

    @Override
    public Map test(String binaryData, boolean verbose) {
        int lengthOfBitString = binaryData.length();
        int count = 0;
        for (int i = 0; i < lengthOfBitString; i++) {
            if (binaryData.charAt(i) == '0') {
                count--;
            } else if (binaryData.charAt(i) == '1') {
                count++;
            }
        }
        double sObs = count / Math.sqrt(lengthOfBitString);
        double pValue = Erf.erfc(Math.abs(sObs) / Math.sqrt(2));

        if (verbose) {
            System.out.println("Frequency Test (Monobit Test) DEBUG BEGIN:");
            System.out.println("\tLength of input:\t" + lengthOfBitString);
            System.out.println("\t# of '0':\t\t\t" + binaryData.chars().filter(c -> c == '0').count());
            System.out.println("\t# of '1':\t\t\t" + binaryData.chars().filter(c -> c == '1').count());
            System.out.println("\tS(n):\t\t\t\t" + count);
            System.out.println("\tsObs:\t\t\t\t" + sObs);
            System.out.println("\tf:\t\t\t\t\t" + Math.abs(sObs) / Math.sqrt(2));
            System.out.println("\tP-Value:\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);
        map.put("testName", "1. Частотный побитовый тест.");

        return map;
    }
}
