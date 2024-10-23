#ifndef CYCLICCONVOLUTIONTHREAD_H
#define CYCLICCONVOLUTIONTHREAD_H

#include "../util/Convolution.h"

template <typename TMatrix, typename TFilter>
void horizontalCyclicConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int startIndex, int step) {
    for (int i = startIndex; i < n; i += step) {
        for (int j = 0; j < m; j++) {
            V[i][j] = convolute(F, n, m, i, j, C, k);
        }
    }
}

template <typename TMatrix, typename TFilter>
void verticalCyclicConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int startIndex, int step) {
    for (int i = 0; i < n; i++) {
        for (int j = startIndex; j < m; j += step) {
            V[i][j] = convolute(F, n, m, i, j, C, k);
        }
    }
}

template <typename TMatrix, typename TFilter>
void deltaCyclicConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int startIndex, int step) {
    for (int l = startIndex; l < n*m; l += step) {
        int i = l / m;
        int j = l % m;
        V[i][j] = convolute(F, n, m, i, j, C, k);
    }
}

#endif //CYCLICCONVOLUTIONTHREAD_H
