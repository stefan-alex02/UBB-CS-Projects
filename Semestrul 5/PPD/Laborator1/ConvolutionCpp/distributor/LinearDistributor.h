#ifndef LINEARDISTRIBUTOR_H
#define LINEARDISTRIBUTOR_H

#include "Distributor.h"

template <typename TMatrix, typename TFilter>
class LinearDistributor : public Distributor<TMatrix, TFilter> {
public:
    void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) override;
};

#endif //LINEARDISTRIBUTOR_H
