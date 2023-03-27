package com.example.nisttestapp.tmp;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.Gamma;

import java.util.Arrays;
import java.util.stream.IntStream;

public class LinearComplexityTest {

    public static double[] pi = {0.01047, 0.03125, 0.125, 0.5, 0.25, 0.0625, 0.020833};

    public static boolean linear_complexity_test(String binary_data, boolean verbose, int block_size) {

        int length_of_binary_data = binary_data.length();
        int degree_of_freedom = 6;

        double t2 = (block_size / 3.0 + 2.0 / 9) / Math.pow(2, block_size);
        double mean = 0.5 * block_size + (1.0 / 36) * (9 + Math.pow(-1, block_size + 1)) - t2;

        int number_of_block = length_of_binary_data / block_size;

        if (number_of_block > 1) {
            int block_end = block_size;
            int block_start = 0;
            String[] blocks = new String[number_of_block];
            for (int i = 0; i < number_of_block; i++) {
                blocks[i] = binary_data.substring(block_start, block_end);
                block_start += block_size;
                block_end += block_size;
            }

            double[] complexities = Arrays.stream(blocks).mapToDouble(LinearComplexityTest::berlekampMasseyAlgorithm)
                    .toArray();

            double[] t = Arrays.stream(complexities).map(chunk -> -1.0 * (Math.pow(-1, block_size) * (chunk - mean) + 2.0 / 9)).toArray();
            int[] vg = new int[7];
            Arrays.stream(t).forEach(chunk -> {
                if (chunk <= -2.5) vg[0]++;
                else if (chunk <= -1.5) vg[1]++;
                else if (chunk <= -0.5) vg[2]++;
                else if (chunk <= 0.5) vg[3]++;
                else if (chunk <= 1.5) vg[4]++;
                else if (chunk <= 2.5) vg[5]++;
                else vg[6]++;
            });
            double[] im = IntStream.range(0, 7).mapToDouble(i -> Math.pow(vg[i] - number_of_block * pi[i], 2) / (number_of_block * pi[i])).toArray();

            double xObs = Arrays.stream(im).sum();

            double p_value = Gamma.regularizedGammaP(degree_of_freedom / 2.0, xObs / 2.0);

            if (verbose) {
                System.out.println("Linear Complexity Test DEBUG BEGIN:");
                System.out.println("\tLength of input:\t" + length_of_binary_data);
                System.out.println("\tLength in bits of a block:\t" + block_size);
                System.out.println("\tDegree of Freedom:\t\t" + degree_of_freedom);
                System.out.println("\tNumber of Blocks:\t" + number_of_block);
                System.out.println("\tValue of Vs:\t\t" + Arrays.toString(vg));
                System.out.println("\txObs:\t\t\t\t" + xObs);
                System.out.println("\tP-Value:\t\t\t" + p_value);
                System.out.println("DEBUG END.");
            }

            return p_value >= 0.01;
        } else {
            return false;
        }
    }

    public static int berlekampMasseyAlgorithm(String blockData) {
        int n = blockData.length();
        double[] c = new double[n];
        double[] b = new double[n];
        c[0] = 1;
        b[0] = 1;
        int l = 0;
        int m = -1;
        int i = 0;
        double[] intData = new double[n];
        for (int j = 0; j < n; j++) {
            intData[j] = Character.getNumericValue(blockData.charAt(j));
        }
        while (i < n) {
            double[] v = Arrays.copyOfRange(intData, (i - l), i);
            v = reverseArray(v);
            double[] cc = Arrays.copyOfRange(c, 1, l + 1);

            RealVector vectorA = new ArrayRealVector(v);
            RealVector vectorB = new ArrayRealVector(cc);
            double dotProduct = vectorA.dotProduct(vectorB);

            double d = (intData[i] + dotProduct) % 2;
            if (d == 1) {
                double[] temp = Arrays.copyOf(c, c.length);
                double[] p = new double[n];
                for (int j = 0; j < l; j++) {
                    if (b[j] == 1) {
                        p[j + i - m] = 1;
                    }
                }
                for (int j = 0; j < n; j++) {
                    c[j] = (c[j] + p[j]) % 2;
                }
                if (l <= 0.5 * i) {
                    l = i + 1 - l;
                    m = i;
                    b = Arrays.copyOf(temp, temp.length);
                }
            }
            i++;
        }
        return l;
    }

    private static double[] reverseArray(double[] array) {
        double[] reversedArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            reversedArray[i] = array[array.length - i - 1];
        }
        return reversedArray;
    }

}
