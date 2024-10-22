package ro.ppd2024.distributor;


import ro.ppd2024.thread.cyclic.DeltaCyclicConvolutionThread;
import ro.ppd2024.thread.cyclic.HorizontalCyclicConvolutionThread;
import ro.ppd2024.thread.cyclic.VerticalCyclicConvolutionThread;
import ro.ppd2024.util.Technique;

public class CyclicDistributor implements Distributor {
    @Override
    public void distribute(int[][] F, int n, int m, int[][] V, int[][] C, int k,
                                  int nrThreads, Technique technique) {
        Thread[] threads = new Thread[nrThreads];

        for (int i = 0; i < nrThreads; i++) {
            switch (technique) {
                case HORIZONTAL_CYCLIC:
                    threads[i] = new HorizontalCyclicConvolutionThread(F, V, n, m, C, k, i, nrThreads);
                    break;
                case VERTICAL_CYCLIC:
                    threads[i] = new VerticalCyclicConvolutionThread(F, V, n, m, C, k, i, nrThreads);
                    break;
                case DELTA_CYCLIC:
                    threads[i] = new DeltaCyclicConvolutionThread(F, V, n, m, C, k, i, nrThreads);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid technique");
            }
            threads[i].start();
        }

        for (int i = 0; i < nrThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
