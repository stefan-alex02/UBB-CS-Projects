package ro.ppd2024;

import ro.ppd2024.distributor.BlockDistributor;
import ro.ppd2024.distributor.CyclicDistributor;
import ro.ppd2024.distributor.Distributor;
import ro.ppd2024.distributor.LinearDistributor;
import ro.ppd2024.util.ResultComparer;
import ro.ppd2024.util.SequentialConvolution;
import ro.ppd2024.util.Technique;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static long performTimer(Runnable runnable, int n, int m) {
        long start = System.nanoTime();
        runnable.run();
        long end = System.nanoTime();
        return end - start;
    }

    public static long performTimerAndAssertion(Runnable runnable, int[][] expected, int[][] actual, int n, int m) {
        long time = performTimer(runnable, n, m);
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

    public static DataSuite readDataFromFile(String filename) {
        DataSuite dataSuite = new DataSuite();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String line = bufferedReader.readLine();
            String[] tokens = line.split(" ");
            dataSuite.n = Integer.parseInt(tokens[0]);
            dataSuite.m = Integer.parseInt(tokens[1]);
            dataSuite.k = Integer.parseInt(tokens[2]);
            dataSuite.F = new int[dataSuite.n][dataSuite.m];
            dataSuite.C = new int[dataSuite.k][dataSuite.k];
            for (int i = 0; i < dataSuite.n; i++) {
                line = bufferedReader.readLine();
                tokens = line.split(" ");
                for (int j = 0; j < dataSuite.m; j++) {
                    dataSuite.F[i][j] = Integer.parseInt(tokens[j]);
                }
            }
            for (int i = 0; i < dataSuite.k; i++) {
                line = bufferedReader.readLine();
                tokens = line.split(" ");
                for (int j = 0; j < dataSuite.k; j++) {
                    dataSuite.C[i][j] = Integer.parseInt(tokens[j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSuite;
    }

    public static void main(String[] args) {
        DataSuite suite = readDataFromFile("src/main/resources/data_1000_1000_5.txt");
//        suite.nrThreads = Integer.parseInt(args[1]);
//        suite.technique = Technique.values()[Integer.parseInt(args[2])];
        suite.nrThreads = Integer.parseInt("8");
        suite.technique = Technique.values()[Integer.parseInt("1")];

        int[][] VSequential = new int[suite.n][suite.m];
        int[][] VParallel = new int[suite.n][suite.m];

        long sequentialTime = performTimer(() ->
                        SequentialConvolution.performTask(suite.F, suite.n, suite.m, VSequential, suite.C, suite.k),
                suite.n, suite.m);

//            System.out.println(technique.name());

        if (suite.technique == Technique.SEQUENTIAL) {
            System.out.println(sequentialTime);
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

            System.out.println(suite.technique.name() + " - Seq: " + sequentialTime + " Par: " + parallelTime);
        }
    }

    static class DataSuite {
        int[][] F;
        int[][] C;
        int n;
        int m;
        int k;
        int nrThreads;
        Technique technique;
    }
}