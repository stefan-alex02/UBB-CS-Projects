#ifndef CYCLICDISTRIBUTOR_H
#define CYCLICDISTRIBUTOR_H

#include "Distributor.h"
#include "../thread/CyclicConvolutionThread.h"
#include <thread>
#include <stdexcept>

template <typename TMatrix, typename TFilter>
class CyclicDistributor : public Distributor<TMatrix, TFilter> {
    void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) override {
        std::thread* threads = new std::thread[nrThreads];

        for (int i = 0; i < nrThreads; i++) {
            switch (technique) {
                case Technique::HORIZONTAL_CYCLIC:
                    threads[i] = std::thread(horizontalCyclicConvolutionThread<TMatrix, TFilter>, F, V, n, m, C, k, i, std::min(nrThreads, n));
                    break;
                case Technique::VERTICAL_CYCLIC:
                    threads[i] = std::thread(verticalCyclicConvolutionThread<TMatrix, TFilter>, F, V, n, m, C, k, i, std::min(nrThreads, m));
                    break;
                case Technique::DELTA_CYCLIC:
                    threads[i] = std::thread(deltaCyclicConvolutionThread<TMatrix, TFilter>, F, V, n, m, C, k, i, nrThreads);
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
};

#endif //CYCLICDISTRIBUTOR_H
