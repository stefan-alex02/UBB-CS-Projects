package ro.ppd2024.util;

public class SequentialConvolution {
    public static void convolute(int[][] F, int n, int m, int[][] V, int[][] C, int k) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                V[i][j] = Convolution.convolute(F, n, m, i, j, C, k);
            }
        }
    }
}
