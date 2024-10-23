#include "CyclicDistributor.h"
#include "../thread/CyclicConvolutionThread.h"
#include <thread>
#include <stdexcept>

template <typename TMatrix, typename TFilter>
void CyclicDistributor<TMatrix, TFilter>::distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) {
    std::thread* threads = new std::thread[nrThreads];

    for (int i = 0; i < nrThreads; i++) {
        switch (technique) {
            case Technique::HORIZONTAL_CYCLIC:
                threads[i] = std::thread(horizontalCyclicConvolutionThread, F, V, n, m, C, k, i, std::min(nrThreads, n));
                break;
            case Technique::VERTICAL_CYCLIC:
                threads[i] = std::thread(verticalCyclicConvolutionThread, F, V, n, m, C, k, i, std::min(nrThreads, m));
                break;
            case Technique::DELTA_CYCLIC:
                threads[i] = std::thread(deltaCyclicConvolutionThread, F, V, n, m, C, k, i, nrThreads);
                break;
            default:
                throw std::invalid_argument("Invalid technique");
        }
    }

    for (int i = 0; i < nrThreads; i++) {
        threads[i].join();
    }
    delete[] threads;
}
