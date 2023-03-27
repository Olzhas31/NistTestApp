package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import org.apache.commons.math3.special.Erf;

import java.util.HashMap;
import java.util.Map;

public class StatisticalTest implements Test {

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        Map<String, Object> map = new HashMap<>();
        map.put("testName", "7. Универсальный статистический тест Маурера");

        int length_of_binary_data = binaryData.length();
        int pattern_size = 5;
//        TODO minimum
        if (length_of_binary_data >= 387840) {
            pattern_size = 6;
        }
        if (length_of_binary_data >= 904960) {
            pattern_size = 7;
        }
        if (length_of_binary_data >= 2068480) {
            pattern_size = 8;
        }
        if (length_of_binary_data >= 4654080) {
            pattern_size = 9;
        }
        if (length_of_binary_data >= 10342400) {
            pattern_size = 10;
        }
        if (length_of_binary_data >= 22753280) {
            pattern_size = 11;
        }
        if (length_of_binary_data >= 49643520) {
            pattern_size = 12;
        }
        if (length_of_binary_data >= 107560960) {
            pattern_size = 13;
        }
        if (length_of_binary_data >= 231669760) {
            pattern_size = 14;
        }
        if (length_of_binary_data >= 496435200) {
            pattern_size = 15;
        }
        if (length_of_binary_data >= 1059061760) {
            pattern_size = 16;
        }

        if (5 < pattern_size && pattern_size < 16) {
            String ones = "";
            for (int i = 0; i < pattern_size; i++) {
                ones += "1";
            }

            int num_ints = Integer.parseInt(ones, 2);
            double[] vobs = new double[num_ints + 1];

            int num_blocks = (int) Math.floor(length_of_binary_data / pattern_size);
            int init_bits = (int) (10 * Math.pow(2, pattern_size));

            int test_bits = num_blocks - init_bits;

            double c = 0.7 - 0.8 / pattern_size + (4 + 32 / pattern_size) * Math.pow(test_bits, -3 / pattern_size) / 15;
            double[] variance = {0, 0, 0, 0, 0, 0, 2.954, 3.125, 3.238, 3.311, 3.356, 3.384, 3.401, 3.410, 3.416, 3.419, 3.421};
            double[] expected = {0, 0, 0, 0, 0, 0, 5.2177052, 6.1962507, 7.1836656, 8.1764248, 9.1723243,
                    10.170032, 11.168765, 12.168070, 13.167693, 14.167488, 15.167379};
            double sigma = c * Math.sqrt(variance[pattern_size] / test_bits);

            double cumsum = 0.0;

            for (int i = 0; i < num_blocks; i++) {
                int block_start = i * pattern_size;
                int block_end = block_start + pattern_size;
                String block_data = binaryData.substring(block_start, block_end);

                int int_rep = Integer.parseInt(block_data, 2);

                if (i < init_bits) {
                    vobs[int_rep] = i + 1;
                } else {
                    double initial = vobs[int_rep];
                    vobs[int_rep] = i + 1;
                    cumsum += Math.log(i - initial + 1) / Math.log(2);
                }
            }
            double phi = cumsum / test_bits;
            double stat = Math.abs(phi - expected[pattern_size]) / (Math.sqrt(2) * sigma);

            double pValue = Erf.erfc(stat);

            if (verbose) {
                System.out.println("Maurer's Universal Statistical Test DEBUG BEGIN:");
                System.out.println("\tLength of input:\t\t" + length_of_binary_data);
                System.out.println("\tLength of each block:\t" + pattern_size);
                System.out.println("\tNumber of Blocks:\t\t" + init_bits);
                System.out.println("\tValue of phi:\t\t\t" + phi);
                System.out.println("\tP-Value:\t\t\t\t" + pValue);
                System.out.println("DEBUG END.");
            }

            map.put("pValue", pValue);

            return map;
        }

        map.put("pValue", -1.0);
        return map;
    }
}
