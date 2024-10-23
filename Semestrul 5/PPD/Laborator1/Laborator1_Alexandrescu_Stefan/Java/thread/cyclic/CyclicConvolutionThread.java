package ro.ppd2024.thread.cyclic;

public abstract class CyclicConvolutionThread extends Thread {
    protected final int[][] F, V, C;
    protected final int n, m, k;
    protected final int startIndex, step;

    protected CyclicConvolutionThread(int[][] f, int[][] v, int n, int m, int[][] c, int k, int startIndex, int step) {
        F = f;
        V = v;
        this.n = n;
        this.m = m;
        C = c;
        this.k = k;
        this.startIndex = startIndex;
        this.step = step;
    }
}
