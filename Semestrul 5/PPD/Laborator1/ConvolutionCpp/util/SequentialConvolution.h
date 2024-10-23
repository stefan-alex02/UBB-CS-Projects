#ifndef SEQUENTIALCONVOLUTION_H
#define SEQUENTIALCONVOLUTION_H

class SequentialConvolution {
public:
    template <typename TMatrix, typename TFilter>
    static void performTask(TMatrix F, int n, int m, TMatrix V, TFilter C, int k);
};

#endif // SEQUENTIALCONVOLUTION_H

