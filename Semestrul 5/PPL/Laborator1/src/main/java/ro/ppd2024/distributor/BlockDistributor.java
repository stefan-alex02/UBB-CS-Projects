package ro.ppd2024.distributor;

import ro.ppd2024.thread.BlockConvolutionThread;
import ro.ppd2024.util.Technique;

public class BlockDistributor implements Distributor {
    @Override
    public void distribute(int[][] F, int n, int m, int[][] V, int[][] C, int k, int nrThreads, Technique technique) {
        distribute(F, n, m, V, C, k, nrThreads);
    }

    private void distribute(int[][] F, int n, int m, int[][] V, int[][] C, int k, int nrThreads) {
        int threadsPerSide = (int) Math.sqrt(nrThreads);
        int blockSizeN = n / threadsPerSide;
        int remainderN = n % threadsPerSide;
        int blockSizeM = m / threadsPerSide;
        int remainderM = m % threadsPerSide;
        Thread[] threads = new Thread[nrThreads];

        int t = 0;
        for (int startRow = 0; startRow < n;) {
            int endRow = startRow + blockSizeN;
            if (remainderN > 0) {
                endRow++;
                remainderN--;
            }
            int tempRemainderM = remainderM;
            for (int startCol = 0; startCol < m;) {
                int endCol = startCol + blockSizeM;
                if (tempRemainderM > 0) {
                    endCol++;
                    tempRemainderM--;
                }

                threads[t] = new BlockConvolutionThread(F, V, n, m, C, k, startRow, endRow, startCol, endCol);
                threads[t].start();
                t++;

                startCol = endCol;
            }
            startRow = endRow;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
