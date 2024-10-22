package ro.ppd2024;

import ro.ppd2024.distributor.BlockDistributor;
import ro.ppd2024.distributor.CyclicDistributor;
import ro.ppd2024.distributor.Distributor;
import ro.ppd2024.distributor.LinearDistributor;
import ro.ppd2024.util.ResultComparer;
import ro.ppd2024.util.SequentialConvolution;
import ro.ppd2024.util.Technique;

import static ro.ppd2024.MatrixFileHandler.readDataFromFile;

public class Main {
    public static long performTimer(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long end = System.nanoTime();
        return end - start;
    }

    public static long performTimerAndAssertion(Runnable runnable, int[][] expected, int[][] actual, int n, int m) {
        long time = performTimer(runnable);
//        printMatrix(expected, n, m);
//        System.out.println();
//        printMatrix(actual, n, m);
        if (!ResultComparer.compare(expected, actual, n, m)) {
            System.out.println("Results are not equal!");
        }
        return time;
    }

    public static void printMatrix(int[][] matrix, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        DataSuite suite = readDataFromFile(args[0]);
        suite.nrThreads = Integer.parseInt(args[1]);
        suite.technique = Technique.values()[Integer.parseInt(args[2])];
//        DataSuite suite = readDataFromFile("src/main/resources/input/data_10_10_3.txt");
//        suite.nrThreads = Integer.parseInt("8");
//        suite.technique = Technique.values()[Integer.parseInt("1")];

        int[][] VSequential = new int[suite.n][suite.m];
        int[][] VParallel = new int[suite.n][suite.m];

        long sequentialTime = performTimer(() ->
                SequentialConvolution.performTask(suite.F, suite.n, suite.m, VSequential, suite.C, suite.k));

        if (suite.technique == Technique.SEQUENTIAL) {
            System.out.println("Time: " + sequentialTime);
        } else {
            Distributor distributor = switch (suite.technique) {
                case HORIZONTAL_LINEAR, VERTICAL_LINEAR, DELTA_LINEAR -> new LinearDistributor();
                case HORIZONTAL_CYCLIC, VERTICAL_CYCLIC, DELTA_CYCLIC -> new CyclicDistributor();
                case BLOCK -> new BlockDistributor();
                default -> throw new IllegalArgumentException("Invalid technique");
            };

            long parallelTime = performTimerAndAssertion(() ->
                    distributor.distribute(suite.F, suite.n, suite.m, VParallel, suite.C,
                            suite.k, suite.nrThreads, suite.technique), VSequential, VParallel, suite.n, suite.m);

            System.out.println(suite.technique.name() + " - Threads: " + suite.nrThreads + " Seq time: " + sequentialTime + " Time: " + parallelTime);

            MatrixFileHandler.printResultToFile(VParallel, suite.n, suite.m,
                    "../../../resources/output/result_" + suite.n + "_" + suite.m + "_" + suite.k + ".txt");
        }
    }

    public static class DataSuite {
        int[][] F;
        int[][] C;
        int n;
        int m;
        int k;
        int nrThreads;
        Technique technique;
    }
}