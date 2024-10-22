package ro.ppd2024.thread.cyclic;

import ro.ppd2024.util.Convolution;

public class DeltaCyclicConvolutionThread extends CyclicConvolutionThread {
    public DeltaCyclicConvolutionThread(int[][] f, int[][] v, int n, int m, int[][] c, int k, int startIndex, int step) {
        super(f, v, n, m, c, k, startIndex, step);
    }

    @Override
    public void run() {
        for (int l = startIndex; l < n; l += step) {
            int i = l / m;
            int j = l % m;
            V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
        }
    }
}
