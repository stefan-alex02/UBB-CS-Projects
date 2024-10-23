#ifndef MATRIXUTILS_H
#define MATRIXUTILS_H

#include <functional>
#include <iostream>
#include "technique.h"

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

double performTimer(const std::function<void()>& func);

template <typename TMatrix>
double performTimerAndAssertion(const std::function<void()>& func, TMatrix expected, TMatrix actual, int n, int m) {
    double time = performTimer(func);
    if (!compareResults(expected, actual, n, m)) {
        std::cout << "Results are not equal!" << std::endl;
    }
    return time;
}

void printMatrix(int** matrix, int n, int m);

template <typename TMatrix, typename TFilter>
struct DataSuite {
    TMatrix F;
    TFilter C;
    int n;
    int m;
    int k;
    int nrThreads;
    Technique technique;
};

// Explicit template instantiation for performTimerAndAssertion
//template double performTimerAndAssertion<int[10000][10000]>(const std::function<void()>&, int[10000][10000], int[10000][10000], int, int);

#endif //MATRIXUTILS_H
