#ifndef LINEARDISTRIBUTOR_H
#define LINEARDISTRIBUTOR_H

#include "Distributor.h"
#include "../thread/LinearConvolutionThread.h"
#include <stdexcept>
#include <thread>

template <typename TMatrix, typename TFilter>
class LinearDistributor : public Distributor<TMatrix, TFilter> {
public:
    void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) override {
        int limit;
        switch (technique) {
            case Technique::HORIZONTAL_LINEAR:
                limit = n;
                break;
            case Technique::VERTICAL_LINEAR:
                limit = m;
                break;
            case Technique::DELTA_LINEAR:
                limit = n * m;
                break;
            default:
                throw std::invalid_argument("Invalid technique");
        }

        nrThreads = std::min(nrThreads, limit);
        int size = limit / nrThreads;
        int remainder = limit % nrThreads;
        int start = 0, end, i = 0;
        std::thread* threads = new std::thread[nrThreads];

        while (start < limit) {
            end = start + size;
            if (remainder > 0) {
                end++;
                remainder--;
            }

            switch(technique) {
                case Technique::HORIZONTAL_LINEAR:
                    threads[i] = std::thread(horizontalLinearConvolutionThread<TMatrix, TFilter>, F, V, n, m, C, k, start, end);
                    break;
                case Technique::VERTICAL_LINEAR:
                    threads[i] = std::thread(verticalLinearConvolutionThread<TMatrix, TFilter>, F, V, n, m, C, k, start, end);;
                    break;
                case Technique::DELTA_LINEAR:
                    threads[i] = std::thread(deltaLinearConvolutionThread<TMatrix, TFilter>, F, V, n, m, C, k, start, end);;
                    break;
            }
            start = end;
            i++;
        }

        for (i = 0; i < nrThreads; i++) {
            threads[i].join();
        }
        delete[] threads;
    }
};

#endif //LINEARDISTRIBUTOR_H
