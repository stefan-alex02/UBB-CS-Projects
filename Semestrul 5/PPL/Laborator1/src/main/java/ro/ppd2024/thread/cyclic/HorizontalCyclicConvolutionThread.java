package ro.ppd2024.thread.cyclic;

import ro.ppd2024.util.Convolution;

public class HorizontalCyclicConvolutionThread extends CyclicConvolutionThread {
    protected HorizontalCyclicConvolutionThread(int[][] f, int[][] v, int n, int m, int[][] c, int k, int startIndex, int step) {
        super(f, v, n, m, c, k, startIndex, step);
    }

    @Override
    public void run() {
        for (int i = startIndex; i < n; i += step) {
            for (int j = 0; j < m; j++) {
                V[i][j] = Convolution.convolute(F, m, n, i, j, C, k);
            }
        }
    }
}
