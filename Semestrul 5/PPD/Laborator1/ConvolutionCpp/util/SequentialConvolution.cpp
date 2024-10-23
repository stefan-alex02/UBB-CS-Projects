#include "SequentialConvolution.h"
#include "Convolution.h"

template <typename TMatrix, typename TFilter>
void SequentialConvolution::performTask(TMatrix F, int n, int m, TMatrix V, TFilter C, int k) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            V[i][j] = convolute(F, n, m, i, j, C, k);
        }
    }
}
