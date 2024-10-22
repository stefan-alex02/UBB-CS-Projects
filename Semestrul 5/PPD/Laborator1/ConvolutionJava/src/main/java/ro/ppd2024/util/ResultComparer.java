package ro.ppd2024.util;

public class ResultComparer {
    public static boolean compare(int[][] A, int[][] B, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (A[i][j] != B[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }
}
