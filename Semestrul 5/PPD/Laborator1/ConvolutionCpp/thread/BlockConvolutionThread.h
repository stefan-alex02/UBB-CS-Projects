#ifndef BLOCKCONVOLUTIONTHREAD_H
#define BLOCKCONVOLUTIONTHREAD_H

#include "../util/convolution.h"

template <typename TMatrix, typename TFilter>
void blockConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k,
                            int startRow, int endRow, int startCol, int endCol) {
    for (int i = startRow; i < endRow; i++) {
        for (int j = startCol; j < endCol; j++) {
            V[i][j] = convolute(F, n, m, i, j, C, k);
        }
    }
}

#endif //BLOCKCONVOLUTIONTHREAD_H
