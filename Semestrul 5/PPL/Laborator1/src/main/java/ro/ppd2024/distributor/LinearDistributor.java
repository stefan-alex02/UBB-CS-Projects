package ro.ppd2024.distributor;


import ro.ppd2024.thread.linear.DeltaLinearConvolutionThread;
import ro.ppd2024.thread.linear.HorizontalLinearConvolutionThread;
import ro.ppd2024.thread.linear.VerticalLinearConvolutionThread;
import ro.ppd2024.util.Technique;

public class LinearDistributor {
    public static void distribute(int[][] F, int n, int m, int[][] V, int[][] C, int k,
                                  int nrThreads, Technique technique) {
        int size = switch (technique) {
            case HORIZONTAL_LINEAR -> n / nrThreads;
            case VERTICAL_LINEAR -> m / nrThreads;
            case DELTA_LINEAR -> n * m / nrThreads;
            default -> throw new IllegalArgumentException("Invalid technique");
        };

        int remainder = n % nrThreads;
        Thread[] threads = new Thread[nrThreads];
        int start = 0, end, i = 0;

        while (start < n) {
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
            threads[start].start();
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
