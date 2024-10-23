#include <iostream>
#include <fstream>
#include <functional>
#include "util/technique.h"
#include "util/matrixUtils.h"
#include "util/matrixFileHandler.h"
#include "util/SequentialConvolution.h"
#include "distributor/Distributor.h"
#include "distributor/LinearDistributor.h"
#include "distributor/CyclicDistributor.h"
#include "distributor/BlockDistributor.h"

void readDataFromFile(const std::string &filename, DataSuite<int**, int**> &dataSuite) {
    try {
        std::ifstream fin(filename);
        if (!fin.is_open()) {
            throw std::runtime_error("Error opening file for reading");
        }
        std::string line;
        if (!std::getline(fin, line)) {
            throw std::runtime_error("Error reading line from file");
        }
        std::istringstream iss(line);
        if (!(iss >> dataSuite.n >> dataSuite.m >> dataSuite.k)) {
            throw std::runtime_error("Error parsing matrix dimensions");
        }
        dataSuite.F = new int *[dataSuite.n];
        dataSuite.C = new int *[dataSuite.k];
        for (int i = 0; i < dataSuite.n; ++i) {
            dataSuite.F[i] = new int[dataSuite.m];
        }
        for (int i = 0; i < dataSuite.k; ++i) {
            dataSuite.C[i] = new int[dataSuite.k];
        }
        readMatrixFromFile<int**>(fin, dataSuite.F, dataSuite.n, dataSuite.m);
        readMatrixFromFile<int**>(fin, dataSuite.C, dataSuite.k, dataSuite.k);
    } catch (const std::exception &e) {
        std::cerr << (e.what() == nullptr ? "" : e.what()) << std::endl;
    }
}

int main(int argc, char *argv[]) {
    DataSuite<int**, int**> suite{};
    readDataFromFile(argv[1], suite);
    suite.nrThreads = std::stoi(argv[2]);
    suite.technique = static_cast<Technique>(std::stoi(argv[3]));
//    readDataFromFile("../data/input/data_10_10_3.txt", suite);
//    suite.nrThreads = std::stoi("2");
//    suite.technique = static_cast<Technique>(std::stoi("5"));

    // Allocate memory for matrices
    int **VSequential = new int *[suite.n];
    int **VParallel = new int *[suite.n];
    for (int i = 0; i < suite.n; i++) {
        VSequential[i] = new int[suite.m]();
        VParallel[i] = new int[suite.m]();
    }

    // Sequential convolution
    double sequentialTime = performTimer([&]() {
        SequentialConvolution::performTask(suite.F, suite.n, suite.m, VSequential, suite.C, suite.k);
    });

    if (suite.technique == Technique::SEQUENTIAL) {
        std::cout << "Seq. time: " << sequentialTime << " ms" << std::endl;
    } else {
        // Parallel convolution based on technique
        Distributor<int**, int**> *distributor;
        switch (suite.technique) {
            case Technique::HORIZONTAL_LINEAR:
            case Technique::VERTICAL_LINEAR:
            case Technique::DELTA_LINEAR:
                distributor = new LinearDistributor<int**, int**>();
                break;
            case Technique::HORIZONTAL_CYCLIC:
            case Technique::VERTICAL_CYCLIC:
            case Technique::DELTA_CYCLIC:
                distributor = new CyclicDistributor<int**, int**>();
                break;
            case Technique::BLOCK:
                distributor = new BlockDistributor<int**, int**>();
                break;
            default:
                throw std::invalid_argument("Invalid technique");
        }

        double parallelTime = performTimerAndAssertion([&]() {
            distributor->distribute(suite.F, suite.n, suite.m, VParallel, suite.C, suite.k, suite.nrThreads,
                                    suite.technique);
        }, VSequential, VParallel, suite.n, suite.m);

        std::cout << suite.technique << " - Threads: " << suite.nrThreads
                  << " Seq. time: " << sequentialTime << " ms, Par. time: " << parallelTime << " ms\n";
        std::fflush(stdout);

        // Save output to file
        std::string resourceFolderPath = "data/output/";
        std::string outputFilePath = resourceFolderPath + "result_" + std::to_string(suite.n) + "_" +
                                     std::to_string(suite.m) + "_" + std::to_string(suite.k) + ".txt";
        printResultToFile(VParallel, suite.n, suite.m, outputFilePath);
    }

    // Clean up memory
    for (int i = 0; i < suite.n; i++) {
        delete[] VSequential[i];
        delete[] VParallel[i];
        delete[] suite.F[i];
    }
    delete[] VSequential;
    delete[] VParallel;
    delete[] suite.F;

    for (int i = 0; i < suite.k; i++) {
        delete[] suite.C[i];
    }
    delete[] suite.C;

    return 0;
}
