#include <iostream>
#include <fstream>
#include "util/technique.h"
#include "util/matrixUtils.h"
#include "util/matrixFileHandler.h"
#include "util/SequentialConvolution.h"
#include "distributor/Distributor.h"
#include "distributor/LinearDistributor.h"
#include "distributor/CyclicDistributor.h"
#include "distributor/BlockDistributor.h"

#define MAX_SIZE 10000

int F[MAX_SIZE][MAX_SIZE];
int C[5][5];
int VSequential[MAX_SIZE][MAX_SIZE];
int VParallel[MAX_SIZE][MAX_SIZE];

void readDataFromFile(const std::string &filename, DataSuite<int[MAX_SIZE][MAX_SIZE], int[5][5]> &dataSuite) {
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
        std::cerr << "File read successfully. Matrix dimensions: " << dataSuite.n << "x" << dataSuite.m << " kernel size: " << dataSuite.k << std::endl;

        readMatrixFromFile<int[MAX_SIZE][MAX_SIZE]>(fin, F, dataSuite.n, dataSuite.m);
        readMatrixFromFile<int[5][5]>(fin, C, dataSuite.k, dataSuite.k);
    } catch (const std::exception &e) {
        std::cerr << (e.what() == nullptr ? "" : e.what()) << std::endl;
    }
}

int main(int argc, char *argv[]) {
    DataSuite<int[MAX_SIZE][MAX_SIZE], int[5][5]> suite{};
    readDataFromFile(argv[1], suite);
    suite.nrThreads = std::stoi(argv[2]);
    suite.technique = static_cast<Technique>(std::stoi(argv[3]));
//    readDataFromFile("../data/input/data_10_10_3.txt", suite);
//    suite.nrThreads = std::stoi("2");
//    suite.technique = static_cast<Technique>(std::stoi("5"));

    // Sequential convolution
    double sequentialTime = performTimer([&]() {
        SequentialConvolution::performTask<int[MAX_SIZE][MAX_SIZE], int[5][5]>
                (F, suite.n, suite.m, VSequential, C, suite.k);
    });

    if (suite.technique == Technique::SEQUENTIAL) {
        std::cout << "Seq. time: " << sequentialTime << " ms\n";
    } else {
        // Parallel convolution based on technique
        Distributor<int[MAX_SIZE][MAX_SIZE], int[5][5]> *distributor;
        switch (suite.technique) {
            case Technique::HORIZONTAL_LINEAR:
            case Technique::VERTICAL_LINEAR:
            case Technique::DELTA_LINEAR:
                distributor = new LinearDistributor<int[MAX_SIZE][MAX_SIZE], int[5][5]>();
                break;
            case Technique::HORIZONTAL_CYCLIC:
            case Technique::VERTICAL_CYCLIC:
            case Technique::DELTA_CYCLIC:
                distributor = new CyclicDistributor<int[MAX_SIZE][MAX_SIZE], int[5][5]>();
                break;
            case Technique::BLOCK:
                distributor = new BlockDistributor<int[MAX_SIZE][MAX_SIZE], int[5][5]>();
                break;
            default:
                throw std::invalid_argument("Invalid technique");
        }

        double parallelTime = performTimerAndAssertion<int[MAX_SIZE][MAX_SIZE]>([&]() {
            distributor->distribute(F, suite.n, suite.m, VParallel, C, suite.k, suite.nrThreads,
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

    return 0;
}
