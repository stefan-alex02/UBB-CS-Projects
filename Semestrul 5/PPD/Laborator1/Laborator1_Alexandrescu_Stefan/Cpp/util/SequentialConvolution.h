#ifndef SEQUENTIALCONVOLUTION_H
#define SEQUENTIALCONVOLUTION_H

#include "Convolution.h"

class SequentialConvolution {
public:
    template <typename TMatrix, typename TFilter>
    static void performTask(TMatrix F, int n, int m, TMatrix V, TFilter C, int k) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                V[i][j] = convolute(F, n, m, i, j, C, k);
            }
        }
    }
};

// Explicit template instantiation for SequentialConvolution::performTask
//template void SequentialConvolution::performTask<int[10000][10000], int[5][5]>(int[10000][10000], int, int, int[10000][10000], int[5][5], int);


#endif // SEQUENTIALCONVOLUTION_H

