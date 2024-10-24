#ifndef CONVOLUTION_H
#define CONVOLUTION_H

#include <algorithm>

template <typename TMatrix, typename TFilter>
int convolute(TMatrix F, int n, int m, int i, int j, TFilter C, int k) {
    int result = 0;
    int halfK = k / 2;

    for (int ki = -halfK; ki <= halfK; ki++) {
        for (int kj = -halfK; kj <= halfK; kj++) {
            int ni = std::min(std::max(i + ki, 0), n - 1);
            int nj = std::min(std::max(j + kj, 0), m - 1);

            result += F[ni][nj] * C[ki + halfK][kj + halfK];
        }
    }

    return result;
}

// Explicit template instantiation
//template int convolute<int[10000][10000], int[5][5]>(int[10000][10000], int, int, int, int, int[5][5], int);

#endif // CONVOLUTION_H
