#ifndef MATRIXFILEHANDLER_H
#define MATRIXFILEHANDLER_H

#include <sstream>
#include <fstream>
#include <string>

template <typename TMatrix>
void writeMatrixToFile(std::ofstream& fout, TMatrix matrix, int n, int m);

template <typename TMatrix>
void readMatrixFromFile(std::ifstream& fin, TMatrix matrix, int n, int m);

template <typename TMatrix>
void printResultToFile(TMatrix result, int n, int m, const std::string& filename);

#endif //MATRIXFILEHANDLER_H
