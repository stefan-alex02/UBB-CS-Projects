#ifndef MATRIXUTILS_H
#define MATRIXUTILS_H

#include <functional>
#include "technique.h"

template <typename TMatrix>
static bool compareResults(TMatrix A, TMatrix B, int n, int m);

double performTimer(const std::function<void()>& func);

template <typename TMatrix>
double performTimerAndAssertion(const std::function<void()>& func, TMatrix expected, TMatrix actual, int n, int m);

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

#endif //MATRIXUTILS_H
