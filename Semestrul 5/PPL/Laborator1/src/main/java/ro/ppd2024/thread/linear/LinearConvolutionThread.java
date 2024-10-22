package ro.ppd2024.thread.linear;

public abstract class LinearConvolutionThread extends Thread {
    protected final int[][] F, V, C;
    protected final int n, m, k;
    protected final int start;
    protected final int end;

    protected LinearConvolutionThread(int[][] F, int[][] V, int n, int m, int[][] C, int k, int start, int end) {
        this.F = F;
        this.V = V;
        this.C = C;
        this.k = k;
        this.start = start;
        this.end = end;
        this.n = n;
        this.m = m;
    }
}