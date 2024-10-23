#ifndef BLOCKDISTRIBUTOR_H
#define BLOCKDISTRIBUTOR_H

#include "Distributor.h"

template <typename TMatrix, typename TFilter>
class BlockDistributor : public Distributor<TMatrix, TFilter> {
public:
    void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) override;
};

#endif // BLOCKDISTRIBUTOR_H
