package ro.ppd2024.thread;

class LinearConvolutionThread extends Thread {
    protected final int[][] F;
    protected final int[][] V;
    protected final int[][] C;
    protected final int k;
    protected final int start;
    protected final int end;
    protected final int n;
    protected final int m;

    public LinearConvolutionThread(int[][] F, int[][] V, int[][] C, int k, int start, int end, int n, int m) {
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