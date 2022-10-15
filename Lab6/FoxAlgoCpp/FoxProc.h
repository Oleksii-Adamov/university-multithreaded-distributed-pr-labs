//
// Created by lvler on 14.10.2022.
//

#ifndef FOXALGOCPP_FOXPROC_H
#define FOXALGOCPP_FOXPROC_H

#include "mpi.h"

class FoxProc {
private:
    int ProcNum = 0; // Number of available processes
    int ProcRank = 0; // Rank of current process
    int Size; // Size of matricies
    int BlockSize; // Sizes of matrix blocks on current process
    int GridSize; // Size of virtual processor grid
    double *pAblock; // Initial block of matrix A on current process
    double *pBblock; // Initial block of matrix B on current process
    double *pCblock; // Block of result matrix C on current process
    double *pMatrixAblock;
    int GridCoords[2]; // Coordinates of current processor in grid
    MPI_Comm GridComm = 0; // Grid communicator
    MPI_Comm ColComm = 0; // Column communicator
    MPI_Comm RowComm = 0; // Row communicator

    void createGridCommunicators();

    void ABlockCommunication (int iter);

    void BblockCommunication();

public:
    FoxProc(int Size, int GridSize);

    ~FoxProc();

    void dataDistribution(double* pAMatrix, double* pBMatrix);

    void parallelResultCalculation();

    void resultCollection(double* pCMatrix);
};


#endif //FOXALGOCPP_FOXPROC_H
