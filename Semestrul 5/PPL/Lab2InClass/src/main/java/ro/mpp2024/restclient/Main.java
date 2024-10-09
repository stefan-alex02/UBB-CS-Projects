package ro.mpp2024.restclient;

import java.util.function.BinaryOperator;

public class Main {
    private static final BinaryOperator<Double> function =
            (a, b) -> Math.pow(a, 2) + Math.pow(b, 2) != 0 ?
                    Math.cbrt((Math.pow(a, 3) + Math.pow(b, 3)) /
                            (Math.pow(a, 2) + Math.pow(b, 2)))
                    : 1;

    static class SumThread extends Thread {
        private final double[] array1;
        private final double[] array2;
        private final double[] sumArray;
        private final int start;
        private final int end;

        public SumThread(double[] array1, double[] array2, double[] sumArray, int start, int end) {
            this.array1 = array1;
            this.array2 = array2;
            this.sumArray = sumArray;
            this.start = start;
            this.end = end;
        }

        public void run() {
            for (int i = start; i < end; i++) {
                sumArray[i] = function.apply(array1[i], array2[i]);
            }
        }
    }

    static class CyclicSumThread extends Thread {
        private final double[] array1;
        private final double[] array2;
        private final double[] sumArray;
        private final int position;
        private final int length;
        private final int noOfThreads;

        public CyclicSumThread(double[] array1, double[] array2, double[] sumArray, int position, int length,
                               int noOfThreads) {
            this.array1 = array1;
            this.array2 = array2;
            this.sumArray = sumArray;
            this.position = position;
            this.length = length;
            this.noOfThreads = noOfThreads;
        }

        @Override
        public void run() {
            for (int i = position; i < length; i += noOfThreads) {
                sumArray[i] = function.apply(array1[i], array2[i]);
            }
        }
    }

    public static double[] sequentialSum(double[] array1, double[] array2, int length) {
        double[] sumArray  = new double[length];
        for (int i = 0; i < length; i++) {
            sumArray[i] = function.apply(array1[i], array2[i]);
        }
        return sumArray;
    }

    public static double[] parallelSum(double[] array1, double[] array2, int length, int noThreads)
            throws InterruptedException {
        double[] sumArray  = new double[length];

        SumThread[] threads = new SumThread[noThreads];

        // distributie liniara echilibrata
        int cat = length / noThreads;
        int rest = length % noThreads;
        int start = 0, end, i = 0;
        while (start < length) {
            end = start + cat;
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new SumThread(array1, array2, sumArray, start, end);
            threads[i].start();
            start = end;
            i++;
        }

        for (i = 0; i < noThreads; i++) {
            threads[i].join();
        }

        return sumArray;
    }

    public static double[] parallelSumLambdaThread(double[] array1, double[] array2, int length, int noThreads)
            throws InterruptedException {
        double[] sumArray  = new double[length];

        Thread[] threads = new Thread[noThreads];

        // distributie liniara echilibrata
        int cat = length / noThreads;
        int rest = length % noThreads;
        int i = 0;
        int start = 0, end;
        while (start < length) {
            end = start + cat;
            if (rest > 0) {
                end++;
                rest--;
            }
            final int startF = start;
            final int endF = end;
            threads[i] = new Thread(() -> {
                for (int ii = startF; ii < endF; ii++) {
                    sumArray[ii] = function.apply(array1[ii], array2[ii]);
                }
            });
            threads[i].start();
            start = end;
            i++;
        }

        for (i = 0; i < noThreads; i++) {
            threads[i].join();
        }

        return sumArray;
    }

    public static double[] parallelCyclicSum(double[] array1, double[] array2, int length, int noThreads)
            throws InterruptedException {
        double[] sumArray  = new double[length];

        CyclicSumThread[] threads = new CyclicSumThread[noThreads];

        for (int i = 0; i < noThreads; i++) {
            threads[i] = new CyclicSumThread(array1, array2, sumArray, i, length, noThreads);
            threads[i].start();
        }

        for (int i = 0; i < noThreads; i++) {
            threads[i].join();
        }

        return sumArray;
    }

    public static void printArray(double[] array) {
        for (double v : array) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    public static boolean assertEqualArrays(double[] array1, double[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        int MAX = 1000000;
        int noThreads = 32;

        double[] array1 = new double[MAX];
        double[] array2 = new double[MAX];

        for (int i = 0; i < MAX; i++) {
            array1[i] = i;
            array2[i] = i;
        }

        System.out.println(Runtime.getRuntime().availableProcessors());

        long startTime = System.nanoTime();
        double[] sumArraySequential = sequentialSum(array1, array2, MAX);
        long endTime = System.nanoTime();

        System.out.println("Sequential:      " + (endTime - startTime));
//        printArray(sumArraySequential);

        startTime = System.nanoTime();
        double[] sumArrayParallel = parallelSum(array1, array2, MAX, noThreads);
        endTime = System.nanoTime();

        System.out.println("Parallel:        " + (endTime - startTime));
//        printArray(sumArrayParallel);

        startTime = System.nanoTime();
        double[] sumArrayParallelLambdaThread = parallelSumLambdaThread(array1, array2, MAX, noThreads);
        endTime = System.nanoTime();

        System.out.println("Parallel lambda: " + (endTime - startTime));

        startTime = System.nanoTime();
        double[] sumArrayParallelCyclic = parallelCyclicSum(array1, array2, MAX, noThreads);
        endTime = System.nanoTime();

        System.out.println("Parallel cyclic: " + (endTime - startTime));

        System.out.println(assertEqualArrays(sumArraySequential, sumArrayParallel));
        System.out.println(assertEqualArrays(sumArrayParallel, sumArrayParallelLambdaThread));
        System.out.println(assertEqualArrays(sumArrayParallel, sumArrayParallelCyclic));
    }
}