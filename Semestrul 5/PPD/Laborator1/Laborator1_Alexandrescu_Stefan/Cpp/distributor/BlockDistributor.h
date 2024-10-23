#ifndef BLOCKDISTRIBUTOR_H
#define BLOCKDISTRIBUTOR_H

#include "Distributor.h"
#include "../thread/BlockConvolutionThread.h"
#include <thread>
#include <cmath>

// Explicit template instantiation
//template class BlockDistributor<int[10000][10000], int[5][5]>;


template <typename TMatrix, typename TFilter>
class BlockDistributor : public Distributor<TMatrix, TFilter> {
public:
    void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) override {
        int threadsPerSide = static_cast<int>(std::sqrt(nrThreads));
        int blockSizeN = n / threadsPerSide;
        int remainderN = n % threadsPerSide;
        int blockSizeM = m / threadsPerSide;
        int remainderM = m % threadsPerSide;

        std::thread* threads = new std::thread[threadsPerSide * threadsPerSide];

        int t = 0;
        for (int startRow = 0; startRow < n;) {
            int endRow = startRow + blockSizeN;
            if (remainderN > 0) {
                endRow++;
                remainderN--;
            }
            int tempRemainderM = remainderM;
            for (int startCol = 0; startCol < m;) {
                int endCol = startCol + blockSizeM;
                if (tempRemainderM > 0) {
                    endCol++;
                    tempRemainderM--;
                }

                threads[t++] = std::thread([&, startRow, endRow, startCol, endCol]() {
                    blockConvolutionThread<TMatrix, TFilter>(F, V, n, m, C, k, startRow, endRow, startCol, endCol);
                });
                startCol = endCol;
            }
            startRow = endRow;
        }

        for (int i = 0; i < threadsPerSide*threadsPerSide; i++) {
            threads[i].join();
        }
        delete[] threads;
    }
};

#endif // BLOCKDISTRIBUTOR_H
