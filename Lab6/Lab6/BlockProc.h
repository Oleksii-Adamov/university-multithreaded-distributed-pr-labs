//
// Created by lvler on 15.10.2022.
//

#ifndef FOXALGOCPP_BLOCKPROC_H
#define FOXALGOCPP_BLOCKPROC_H


#include "Proc.h"
#include "mpi.h"

class BlockProc : public Proc {
protected:
    int Size; // Size of matricies
    int BlockSize; // Sizes of matrix blocks on current process
    int GridSize; // Size of virtual processor grid
    double *pAblock; // Initial block of matrix A on current process
    double *pBblock; // Initial block of matrix B on current process
    double *pCblock; // Block of result matrix C on current process
    int GridCoords[2]; // Coordinates of current processor in grid
    MPI_Comm GridComm = 0; // Grid communicator
    MPI_Comm ColComm = 0; // Column communicator
    MPI_Comm RowComm = 0; // Row communicator

    void createGridCommunicators();

    void BblockCommunication();

public:

    BlockProc(int Size, int GridSize);

    virtual ~BlockProc();

    virtual void dataDistribution(double* pAMatrix, double* pBMatrix) = 0;

    virtual void parallelResultCalculation()  = 0;

    virtual void resultCollection(double* pCMatrix);
};


#endif //FOXALGOCPP_BLOCKPROC_H
