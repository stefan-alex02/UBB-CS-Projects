package ro.ppd2024.thread;

import ro.ppd2024.util.Convolution;

public class BlockConvolutionThread extends Thread {
    private final int[][] F, V, C;
    private final int n, m, k;
    private final int startRow, endRow, startCol, endCol;

    public BlockConvolutionThread(int[][] F, int[][] V, int n, int m, int[][] C, int k, int startRow, int endRow, int startCol, int endCol) {
        this.F = F;
        this.V = V;
        this.C = C;
        this.n = n;
        this.m = m;
        this.k = k;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
            }
        }
    }
}
