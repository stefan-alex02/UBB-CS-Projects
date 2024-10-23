#ifndef CONVOLUTION_H
#define CONVOLUTION_H

template <typename TMatrix, typename TFilter>
int convolute(TMatrix F, int n, int m, int i, int j, TFilter C, int k);

#endif // CONVOLUTION_H
