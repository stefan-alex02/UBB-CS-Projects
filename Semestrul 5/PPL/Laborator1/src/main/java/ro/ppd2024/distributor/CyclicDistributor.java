package ro.ppd2024.distributor;


import ro.ppd2024.thread.cyclic.HorizontalCyclicConvolutionThread;
import ro.ppd2024.thread.linear.DeltaLinearConvolutionThread;
import ro.ppd2024.thread.linear.HorizontalLinearConvolutionThread;
import ro.ppd2024.thread.linear.VerticalLinearConvolutionThread;
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
                    threads[i] = new VerticalLinearConvolutionThread(F, V, n, m, C, k, i, nrThreads);
                    break;
                case DELTA_CYCLIC:
                    threads[i] = new DeltaLinearConvolutionThread(F, V, n, m, C, k, i, nrThreads);
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
