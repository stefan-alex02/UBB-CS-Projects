package ro.ppd2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixFileHandler {
    public static int[][] generate(int n, int m, int max, int min) {
        int[][] matrix = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = (int) (Math.random() * (max - min + 1) + min);
            }
        }

        return matrix;
    }

    public static void writeMatrixToFile(FileWriter fileWriter, int[][] matrix, int n, int m) throws IOException {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                fileWriter.write(matrix[i][j] + " ");
            }
            fileWriter.write("\n");
        }
    }

    public static void readMatrixFromFile(BufferedReader bufferedReader, int[][] matrix, int n, int m) throws IOException {
        for (int i = 0; i < n; i++) {
            String line = bufferedReader.readLine();
            String[] tokens = line.split(" ");
            for (int j = 0; j < m; j++) {
                matrix[i][j] = Integer.parseInt(tokens[j]);
            }
        }
    }

    public static void generateFile(int n, int m, int k, int max, int min) {
        int[][] matrix = generate(n, m, max, min);
        int[][] filter = generate(k, k, max, min);

        // writing to file with format "data_[n]_[m]_[k].txt", in resources folder
        try {
            FileWriter fileWriter =
                    new FileWriter("src/main/resources/input/data_" + n + "_" + m + "_" + k + ".txt");
            fileWriter.write(n + " " + m + " " + k + "\n");
            writeMatrixToFile(fileWriter, matrix, n, m);
            writeMatrixToFile(fileWriter, filter, k, k);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printResultToFile(int[][] result, int n, int m, String filename) {
        try {
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(n + " " + m + "\n");
            writeMatrixToFile(fileWriter, result, n, m);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main.DataSuite readDataFromFile(String filename) {
        Main.DataSuite dataSuite = new Main.DataSuite();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String line = bufferedReader.readLine();
            String[] tokens = line.split(" ");
            dataSuite.n = Integer.parseInt(tokens[0]);
            dataSuite.m = Integer.parseInt(tokens[1]);
            dataSuite.k = Integer.parseInt(tokens[2]);
            dataSuite.F = new int[dataSuite.n][dataSuite.m];
            dataSuite.C = new int[dataSuite.k][dataSuite.k];
            readMatrixFromFile(bufferedReader, dataSuite.F, dataSuite.n, dataSuite.m);
            readMatrixFromFile(bufferedReader, dataSuite.C, dataSuite.k, dataSuite.k);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSuite;
    }

    public static void main(String[] args) {
        int min = 0;
        int max = 100;

        generateFile(10, 10, 3, min, max);
        generateFile(1000, 1000, 5, min, max);
        generateFile(10, 10000, 5, min, max);
        generateFile(10000, 10, 5, min, max);
        generateFile(10000, 10000, 5, min, max);
    }
}
