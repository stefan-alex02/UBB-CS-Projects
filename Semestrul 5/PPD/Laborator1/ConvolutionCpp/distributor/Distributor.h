#ifndef DISTRIBUTOR_H
#define DISTRIBUTOR_H

#include "../util/technique.h"

template <typename TMatrix, typename TFilter>
class Distributor {
public:
    virtual void distribute(TMatrix F, int n, int m, TMatrix V, TFilter C, int k, int nrThreads, Technique technique) = 0;
};

#endif // DISTRIBUTOR_H
