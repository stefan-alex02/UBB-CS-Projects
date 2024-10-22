package ro.ppd2024;

public class Convolution {
    public static int convolute(int[][] F, int n, int m, int i, int j, int[][] C, int k) {
        int result = 0, a;

        for (int ii = i - k/2; ii < i + k/2 + 1; ii++) {
            for (int jj = j - k/2; jj < j + k/2 + 1; jj++) {
                if (0 <= ii && ii < n && 0 <= jj && jj < m) {
                    a = F[ii][jj];
                }
                else if (0 < ii && ii < n) {
                    a =  F[ii][jj < 0 ? 0 : m-1];
                }
                else if (0 < jj && jj < m) {
                    a =  F[ii < 0 ? 0 : n-1][jj];
                }
                else {
                    a = F[ii < 0 ? 0 : n-1][jj < 0 ? 0 : m-1];
                }
                result += a;
            }
        }

        return result;
    }
}
