package ro.ppd2024.distributor;


import ro.ppd2024.thread.linear.DeltaLinearConvolutionThread;
import ro.ppd2024.thread.linear.HorizontalLinearConvolutionThread;
import ro.ppd2024.thread.linear.VerticalLinearConvolutionThread;
import ro.ppd2024.util.Technique;

public class LinearDistributor implements Distributor {
    @Override
    public void distribute(int[][] F, int n, int m, int[][] V, int[][] C, int k,
                                  int nrThreads, Technique technique) {
        int limit = switch  (technique) {
            case HORIZONTAL_LINEAR -> n;
            case VERTICAL_LINEAR -> m;
            case DELTA_LINEAR -> n * m;
            default -> throw new IllegalArgumentException("Invalid technique");
        };
        nrThreads = Math.min(nrThreads, limit);
        int size = limit / nrThreads;
        int remainder = limit % nrThreads;
        Thread[] threads = new Thread[nrThreads];
        int start = 0, end, i = 0;

        while (start < limit) {
            end = start + size;
            if (remainder > 0) {
                end++;
                remainder--;
            }

            switch (technique) {
                case HORIZONTAL_LINEAR:
                    threads[i] = new HorizontalLinearConvolutionThread(F, V, n, m, C, k, start, end);
                    break;
                case VERTICAL_LINEAR:
                    threads[i] = new VerticalLinearConvolutionThread(F, V, n, m, C, k, start, end);
                    break;
                case DELTA_LINEAR:
                    threads[i] = new DeltaLinearConvolutionThread(F, V, n, m, C, k, start, end);
                    break;
            }
            threads[i].start();
            start = end;
            i++;
        }

        for (i = 0; i < nrThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
