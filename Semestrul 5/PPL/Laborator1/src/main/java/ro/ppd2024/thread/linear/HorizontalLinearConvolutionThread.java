package ro.ppd2024.thread.linear;

import ro.ppd2024.Convolution;

public class HorizontalLinearConvolutionThread extends LinearConvolutionThread{
    public HorizontalLinearConvolutionThread(int[][] F, int[][] V, int n, int m, int[][] C, int k, int start, int end) {
        super(F, V, n, m, C, k, start, end);
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
