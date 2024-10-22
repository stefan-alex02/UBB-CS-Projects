package ro.ppd2024;

public class Convolution {
    public static int convolute(int[][] F, int n, int m, int i, int j, int[][] C, int k) {
        int result = 0;
        int halfK = k / 2;

        for (int ki = -halfK; ki <= halfK; ki++) {
            for (int kj = -halfK; kj <= halfK; kj++) {
                int ni = Math.min(Math.max(i + ki, 0), n - 1);
                int nj = Math.min(Math.max(j + kj, 0), m - 1);

                result += F[ni][nj] * C[ki + halfK][kj + halfK];
            }
        }

        return result;
    }
}
