#ifndef LINEARCONVOLUTIONTHREAD_H
#define LINEARCONVOLUTIONTHREAD_H

#include "../util/Convolution.h"

template <typename TMatrix, typename TFilter>
void horizontalLinearConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int start, int end) {
    for (int i = start; i < end; i++) {
        for (int j = 0; j < m; j++) {
            V[i][j] = convolute(F, n, m, i, j, C, k);
        }
    }
}

template <typename TMatrix, typename TFilter>
void verticalLinearConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int start, int end) {
    for (int i = 0; i < n; i++) {
        for (int j = start; j < end; j++) {
            V[i][j] = convolute(F, n, m, i, j, C, k);
        }
    }
}

template <typename TMatrix, typename TFilter>
void deltaLinearConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int start, int end) {
    for (int l = start; l < end; l++) {
        int i = l / m;
        int j = l % m;
        V[i][j] = convolute(F, n, m, i, j, C, k);
    }
}

#endif //LINEARCONVOLUTIONTHREAD_H
