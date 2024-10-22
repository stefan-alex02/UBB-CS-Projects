package ro.ppd2024.thread;

public class HorizontalLinearConvolutionThread extends LinearConvolutionThread{
    public HorizontalLinearConvolutionThread(int[][] F, int[][] V, int[][] C, int start, int end, int n, int m) {
        super(F, V, C, start, end, n, m);
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < m; j++) {
                int sum = 0;
                for (int ki = -1; ki <= 1; ki++) {
                    for (int kj = -1; kj <= 1; kj++) {
                        int ni = Math.min(Math.max(i + ki, 0), n - 1);
                        int nj = Math.min(Math.max(j + kj, 0), m - 1);
                        sum += F[ni][nj] * C[ki + 1][kj + 1];
                    }
                }
                V[i][j] = sum;
            }
        }
    }
}
