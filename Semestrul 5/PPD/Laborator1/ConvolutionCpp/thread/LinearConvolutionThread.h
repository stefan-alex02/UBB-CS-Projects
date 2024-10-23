#ifndef LINEARCONVOLUTIONTHREAD_H
#define LINEARCONVOLUTIONTHREAD_H

template <typename TMatrix, typename TFilter>
void horizontalLinearConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int start, int end);

template <typename TMatrix, typename TFilter>
void verticalLinearConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int start, int end);

template <typename TMatrix, typename TFilter>
void deltaLinearConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k, int start, int end);


#endif //LINEARCONVOLUTIONTHREAD_H
