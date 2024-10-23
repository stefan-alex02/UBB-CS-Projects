#include "matrixUtils.h"
#include <iostream>
#include <chrono>
#include <functional>

template <typename TMatrix>
bool compareResults(TMatrix A, TMatrix B, int n, int m) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (A[i][j] != B[i][j]) {
                return false;
            }
        }
    }
    return true;
}

double performTimer(const std::function<void()>& func) {
    auto start = std::chrono::high_resolution_clock::now();
    func();
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> duration = end - start;
    return duration.count();
}

template <typename TMatrix>
double performTimerAndAssertion(const std::function<void()>& func, TMatrix expected, TMatrix actual, int n, int m) {
    double time = performTimer(func);
    if (!compareResults(expected, actual, n, m)) {
        std::cout << "Results are not equal!" << std::endl;
    }
    return time;
}

void printMatrix(int** matrix, int n, int m) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            std::cout << matrix[i][j] << " ";
        }
        std::cout << std::endl;
    }
}