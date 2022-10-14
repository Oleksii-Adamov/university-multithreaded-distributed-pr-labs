#ifndef FOXALGOCPP_FOXALGO_H
#define FOXALGOCPP_FOXALGO_H

#include <stdexcept>
#include "mpi.h"

class FoxAlgo {
private:
    double* pAMatrix; // The first argument of matrix multiplication (null for Proc != 0)
    double* pBMatrix; // The second argument of matrix multiplication (null for Proc != 0)
    double* pCMatrix; // The result matrix
    int Size; // Size of matricies
    int BlockSize; // Sizes of matrix blocks on current process
    int ProcNum = 0; // Number of available processes
    int ProcRank = 0; // Rank of current process
    int GridSize; // Size of virtual processor grid
    int GridCoords[2]; // Coordinates of current processor in grid
    MPI_Comm GridComm; // Grid communicator
    MPI_Comm ColComm; // Column communicator
    MPI_Comm RowComm; // Row communicator
    double mean_time = 0.0;
    bool correct = true;

public:
    FoxAlgo();

    ~FoxAlgo();

    void execute(int times);

    // meaninful only for ProcRank = 0
    bool isCorrect();

    // meaninful only for ProcRank = 0
    double getMeanTime();
};


#endif //FOXALGOCPP_FOXALGO_H
