package ro.ppd2024.distributor;

import ro.ppd2024.util.Technique;

public interface Distributor {
    void distribute(int[][] F, int n, int m, int[][] V, int[][] C, int k, int nrThreads, Technique technique);
}
