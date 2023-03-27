package com.example.nisttestapp.tmp;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class SpectralTest {

    public static boolean spectralTest(String binaryData, boolean verbose) {
        int lengthOfBinaryData = binaryData.length();
        double[] plusOneMinusOne = new double[lengthOfBinaryData];
        for (int i = 0; i < lengthOfBinaryData; i++) {
            char c = binaryData.charAt(i);
            if (c == '0') {
                plusOneMinusOne[i] = -1;
            } else if (c == '1') {
                plusOneMinusOne[i] = 1;
            }
        }
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] spectral = transformer.transform(plusOneMinusOne, TransformType.FORWARD);

        int slice = (int) Math.floor(lengthOfBinaryData / 2.0);
        double[] modulus = new double[slice];
        for (int i = 0; i < slice; i++) {
            modulus[i] = spectral[i].abs();
        }
        double tau = Math.sqrt(Math.log(1 / 0.05) * lengthOfBinaryData);
        double n0 = 0.95 * (lengthOfBinaryData / 2.0);
        int n1 = 0;
        for (double m : modulus) {
            if (m < tau) {
                n1++;
            }
        }
        double d = (n1 - n0) / Math.sqrt(lengthOfBinaryData * 0.95 * 0.05 / 4.0);
        double pValue = Erf.erfc(Math.abs(d) / Math.sqrt(2));
        if (verbose) {
            System.out.println("Discrete Fourier Transform (Spectral) Test DEBUG BEGIN:");
            System.out.println("\tLength of Binary Data:\t" + lengthOfBinaryData);
            System.out.println("\tValue of T:\t\t\t\t" + tau);
            System.out.println("\tValue of n1:\t\t\t" + n1);
            System.out.println("\tValue of n0:\t\t\t" + n0);
            System.out.println("\tValue of d:\t\t\t\t" + d);
            System.out.println("\tP-Value:\t\t\t\t" + pValue);
            System.out.println("DEBUG END.");
        }

        return pValue >= 0.01;
    }

}
