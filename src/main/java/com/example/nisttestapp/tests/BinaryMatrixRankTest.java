package com.example.nisttestapp.tests;

import com.example.nisttestapp.model.Test;
import com.example.nisttestapp.util.BinaryMatrix;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BinaryMatrixRankTest implements Test {

    // TODO
    private int rowsInMatrix = 32;
//    private int rowsInMatrix = 3;
    private int columnsInMatrix = 32;
//    private int columnsInMatrix = 3;

    @Override
    public Map<String, Object> test(String binaryData, boolean verbose) {
        verbose = true;
        int[] maxRanks = {0, 0, 0};
        int lengthOfBinaryData = binaryData.length();
        int blockSize = rowsInMatrix * columnsInMatrix;
        int numberOfBlocks = (int) Math.floor((double) lengthOfBinaryData / blockSize);
        int blockStart = 0;
        int blockEnd = blockSize;
        double pValue = 0;

        if (numberOfBlocks > 0) {
            for (int im = 0; im < numberOfBlocks; im++) {
                String blockData = binaryData.substring(blockStart, blockEnd);
                double[] block = new double[blockData.length()];

                for (int count = 0; count < blockData.length(); count++) {
                    if (blockData.charAt(count) == '1') {
                        block[count] = 1.0;
                    }
                }

                double[][] matrix = new double[rowsInMatrix][columnsInMatrix];
                int idx = 0;
                for (int i = 0; i < rowsInMatrix; i++) {
                    for (int j = 0; j < columnsInMatrix; j++) {
                        matrix[i][j] = block[idx];
                        idx++;
                    }
                }

                BinaryMatrix ranker = new BinaryMatrix(matrix, rowsInMatrix, columnsInMatrix);
                int rank = ranker.computeRank(verbose);

                if (rank == rowsInMatrix) {
                    maxRanks[0]++;
                } else if (rank == rowsInMatrix - 1) {
                    maxRanks[1]++;
                } else {
                    maxRanks[2]++;
                }

                blockStart += blockSize;
                blockEnd += blockSize;
            }

            double[] pi = new double[3];
            pi[0] = 1.0;
            for (int x = 1; x < 50; x++) {
                pi[0] *= 1 - (1.0 / Math.pow(2, x));
            }
            pi[1] = 2 * pi[0];
            pi[2] = 1 - pi[0] - pi[1];

            double xObs = 0.0;
            for (int i = 0; i < pi.length; i++) {
                xObs += Math.pow((maxRanks[i] - pi[i] * numberOfBlocks), 2.0) / (pi[i] * numberOfBlocks);
            }

            pValue = Math.exp(-xObs / 2);

            if (verbose) {
                System.out.println("Binary Matrix Rank Test DEBUG BEGIN:");
                System.out.println("\tLength of input:\t" + lengthOfBinaryData);
                System.out.println("\tSize of Row:\t\t" + rowsInMatrix);
                System.out.println("\tSize of Column:\t\t" + columnsInMatrix);
                System.out.println("\tValue of N:\t\t\t" + numberOfBlocks);
                System.out.println("\tValue of Pi:\t\t" + Arrays.toString(pi));
                System.out.println("\tValue of xObs:\t\t" + xObs);
                System.out.println("\tP-Value:\t\t\t" + pValue);
                System.out.println("DEBUG END.");
            }

        } else {
//            TODO
//            return false
        }

        Map<String, Object> map = new HashMap<>();
        map.put("pValue", pValue);
        map.put("testName", "5. Тест рангов бинарных матриц");

        return map;
    }
}
