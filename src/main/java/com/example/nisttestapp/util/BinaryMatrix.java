package com.example.nisttestapp.util;

import java.util.Arrays;

public class BinaryMatrix {
    private int M;
    private int Q;
    private double[][] A;
    private int m;

    public BinaryMatrix(double[][] matrix, int rows, int cols) {
        M = rows;
        Q = cols;
        A = matrix;
        m = Math.min(rows, cols);
    }

    public int computeRank(boolean verbose) {
        if (verbose) {
            System.out.println("Original Matrix\n" + Arrays.deepToString(A));
        }

        int i = 0;
        while (i < m - 1) {
            if (A[i][i] == 1) {
                performRowOperations(i, true);
            } else {
                int found = findUnitElementSwap(i, true);
                if (found == 1) {
                    performRowOperations(i, true);
                }
            }
            i++;
        }

        if (verbose) {
            System.out.println("Intermediate Matrix\n" + Arrays.deepToString(A));
        }

        i = m - 1;
        while (i > 0) {
            if (A[i][i] == 1) {
                performRowOperations(i, false);
            } else {
                if (findUnitElementSwap(i, false) == 1) {
                    performRowOperations(i, false);
                }
            }
            i--;
        }

        if (verbose) {
            System.out.println("Final Matrix\n" + Arrays.deepToString(A));
        }

        return determineRank();
    }

    private void performRowOperations(int i, boolean forwardElimination) {
        if (forwardElimination) {
            int j = i + 1;
            while (j < M) {
                if (A[j][i] == 1) {
                    for (int k = 0; k < Q; k++) {
                        A[j][k] = (A[j][k] + A[i][k]) % 2;
                    }
                }
                j++;
            }
        } else {
            int j = i - 1;
            while (j >= 0) {
                if (A[j][i] == 1) {
                    for (int k = 0; k < Q; k++) {
                        A[j][k] = (A[j][k] + A[i][k]) % 2;
                    }
                }
                j--;
            }
        }
    }

    private int findUnitElementSwap(int i, boolean forwardElimination) {
        int rowOp = 0;
        if (forwardElimination) {
            int index = i + 1;
            while (index < M && A[index][i] == 0) {
                index++;
            }
            if (index < M) {
                rowOp = swapRows(i, index);
            }
        } else {
            int index = i - 1;
            while (index >= 0 && A[index][i] == 0) {
                index--;
            }
            if (index >= 0) {
                rowOp = swapRows(i, index);
            }
        }
        return rowOp;
    }

    private int swapRows(int i, int ix) {
        double[] temp = Arrays.copyOf(A[i], Q);
        A[i] = Arrays.copyOf(A[ix], Q);
        A[ix] = Arrays.copyOf(temp, Q);
        return 1;
    }

    private int determineRank() {
        int rank = m;
        int i = 0;
        while (i < M) {
            boolean allZeros = true;
            for (int j = 0; j < Q; j++) {
                if (A[i][j] == 1)
                    allZeros = false;
            }
            if (allZeros) {
                rank -= 1;
            }
            i += 1;
        }
        return rank;
    }
}
