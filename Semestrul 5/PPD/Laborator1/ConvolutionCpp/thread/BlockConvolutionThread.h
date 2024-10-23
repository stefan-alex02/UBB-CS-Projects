#ifndef BLOCKCONVOLUTIONTHREAD_H
#define BLOCKCONVOLUTIONTHREAD_H

template <typename TMatrix, typename TFilter>
void blockConvolutionThread(TMatrix F, TMatrix V, int n, int m, TFilter C, int k,
                            int startRow, int endRow, int startCol, int endCol);

#endif //BLOCKCONVOLUTIONTHREAD_H
