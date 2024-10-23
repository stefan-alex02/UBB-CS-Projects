#ifndef MATRIXFILEHANDLER_H
#define MATRIXFILEHANDLER_H

#include <fstream>
#include <string>
#include <iostream>
#include <sstream>

template <typename TMatrix>
void writeMatrixToFile(std::ofstream& fileWriter, TMatrix matrix, int n, int m) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            fileWriter << matrix[i][j] << " ";
        }
        fileWriter << "\n";
    }
}

template <typename TMatrix>
void readMatrixFromFile(std::ifstream& fin, TMatrix matrix, int n, int m) {
    std::string line;
    for (int i = 0; i < n; i++) {
        if (!std::getline(fin, line)) {
            throw std::runtime_error("Error reading line from file");
        }
        std::istringstream iss(line);
        for (int j = 0; j < m; j++) {
            if (!(iss >> matrix[i][j])) {
                throw std::runtime_error("Error parsing matrix element");
            }
        }
    }
}

template <typename TMatrix>
void printResultToFile(TMatrix result, int n, int m, const std::string& filename) {
    try {
        std::ofstream fout(filename);
        if (!fout.is_open()) {
            throw std::runtime_error("Error opening file for writing");
        }
        fout << n << " " << m << "\n";
        writeMatrixToFile(fout, result, n, m);
        fout.close();
    } catch (const std::exception& e) {
        std::cerr << (e.what() == nullptr ? "" : e.what()) << std::endl;
    }
}

#endif //MATRIXFILEHANDLER_H
