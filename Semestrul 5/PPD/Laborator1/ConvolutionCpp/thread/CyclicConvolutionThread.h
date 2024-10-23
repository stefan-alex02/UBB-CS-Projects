#ifndef CYCLICCONVOLUTIONTHREAD_H
#define CYCLICCONVOLUTIONTHREAD_H

template <typename TMatrix, typename TFilter>
void horizontalCyclicConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int startIndex, int step);

template <typename TMatrix, typename TFilter>
void verticalCyclicConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int startIndex, int step);

template <typename TMatrix, typename TFilter>
void deltaCyclicConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int startIndex, int step);

#endif //CYCLICCONVOLUTIONTHREAD_H
