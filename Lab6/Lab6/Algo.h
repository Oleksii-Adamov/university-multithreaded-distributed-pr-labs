#ifndef FOXALGOCPP_ALGO_H
#define FOXALGOCPP_ALGO_H

#include <stdexcept>
#include "mpi.h"

enum class AlgoType {
    Seqential,
    Tape,
    Fox,
    Cannon
};

class Algo {
private:
    double* pAMatrix; // The first argument of matrix multiplication (null for Proc != 0)
    double* pBMatrix; // The second argument of matrix multiplication (null for Proc != 0)
    double* pCMatrix; // The result matrix
    int Size; // Size of matricies
    int ProcNum = 0; // Number of available processes
    int ProcRank = 0; // Rank of current process
    int GridSize; // Size of virtual processor grid
    double mean_time = 0.0;
    bool correct = true;
    AlgoType algoType;

public:
    Algo(AlgoType algoType);

    ~Algo();

    void execute(int times);

    // meaninful only for ProcRank = 0
    bool isCorrect();

    // meaninful only for ProcRank = 0
    double getMeanTime();
};


#endif //FOXALGOCPP_ALGO_H
