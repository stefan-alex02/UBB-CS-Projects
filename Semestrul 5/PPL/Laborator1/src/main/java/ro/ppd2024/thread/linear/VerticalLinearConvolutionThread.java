package ro.ppd2024.thread.linear;

import ro.ppd2024.Convolution;

public class VerticalLinearConvolutionThread extends LinearConvolutionThread{
    public VerticalLinearConvolutionThread(int[][] F, int[][] V, int n, int m, int[][] C, int k, int start, int end) {
        super(F, V, n, m, C, k, start, end);
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            for (int j = start; j < end; j++) {
                V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
            }
        }
    }
}
