package ro.ppd2024.thread.linear;

import ro.ppd2024.util.Convolution;

public class DeltaLinearConvolutionThread extends LinearConvolutionThread {
    public DeltaLinearConvolutionThread(int[][] F, int[][] V, int n, int m, int[][] C, int k, int start, int end) {
        super(F, V, n, m, C, k, start, end);
    }

    @Override
    public void run() {
        for (int l = start; l < end; l++) {
            int i = l / m;
            int j = l % m;
            V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
        }
    }
}
