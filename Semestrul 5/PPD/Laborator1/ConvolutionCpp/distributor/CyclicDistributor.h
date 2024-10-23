#ifndef CYCLICDISTRIBUTOR_H
#define CYCLICDISTRIBUTOR_H

#include "Distributor.h"

template <typename TMatrix, typename TFilter>
class CyclicDistributor : public Distributor<TMatrix, TFilter> {
    void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) override;
};

#endif //CYCLICDISTRIBUTOR_H
