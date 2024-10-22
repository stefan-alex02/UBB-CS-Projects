package ro.ppd2024.thread.cyclic;

import ro.ppd2024.util.Convolution;

public class VerticalCyclicConvolutionThread extends CyclicConvolutionThread {
    public VerticalCyclicConvolutionThread(int[][] f, int[][] v, int n, int m, int[][] c, int k, int startIndex, int step) {
        super(f, v, n, m, c, k, startIndex, step);
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            for (int j = startIndex; j < m; j += step) {
                V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
            }
        }
    }
}
