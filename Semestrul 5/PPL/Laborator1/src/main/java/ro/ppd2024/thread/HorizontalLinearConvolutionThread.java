package ro.ppd2024.thread;

import ro.ppd2024.Convolution;

public class HorizontalLinearConvolutionThread extends LinearConvolutionThread{
    public HorizontalLinearConvolutionThread(int[][] F, int[][] V, int[][] C, int k, int start, int end, int n, int m) {
        super(F, V, C, k, start, end, n, m);
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < m; j++) {
                V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
            }
        }
    }
}
