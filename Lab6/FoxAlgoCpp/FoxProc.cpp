//
// Created by lvler on 14.10.2022.
//

#include <iostream>
#include "FoxProc.h"
#include "util.h"

FoxProc::FoxProc(int Size, int BlockSize, int GridSize) : Size(Size), BlockSize(BlockSize) {
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    std::cout << ProcRank << "\n";
    pAblock = new double [BlockSize*BlockSize];
    pBblock = new double [BlockSize*BlockSize];
    pCblock = new double [BlockSize*BlockSize];
    pMatrixAblock = new double [BlockSize*BlockSize];
    zeroFill(pCblock, BlockSize * BlockSize);
    createGridCommunicators();
}

FoxProc::~FoxProc() {
    delete[] pAblock;
    delete[] pBblock;
    delete[] pCblock;
    delete[] pMatrixAblock;
}

void FoxProc::ABlockCommunication(int iter) {
    // Defining the leading process of the process grid row
    int Pivot = (GridCoords[0] + iter) % GridSize;
// Copying the transmitted block in a separate memory buffer
    if (GridCoords[1] == Pivot) {
        copyArr(pMatrixAblock, pAblock, BlockSize * BlockSize);
    }
// Block broadcasting
    MPI_Bcast(pAblock, BlockSize * BlockSize, MPI_DOUBLE, Pivot, RowComm);
}

void FoxProc::BblockCommunication() {
    MPI_Status Status;
    int NextProc = GridCoords[0] + 1;
    if ( GridCoords[0] == GridSize-1 ) NextProc = 0;
    int PrevProc = GridCoords[0] - 1;
    if ( GridCoords[0] == 0 ) PrevProc = GridSize-1;
    MPI_Sendrecv_replace( pBblock, BlockSize*BlockSize, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, ColComm, &Status);
}

void FoxProc::dataDistribution(double *pAMatrix, double *pBMatrix) {
    if (ProcRank == 0) {
        for (int ProcRec = 1; ProcRec < ProcNum; ProcRec++) {
            int row_start = (ProcRec / 2) * BlockSize, column_start = (ProcRec % 2) * BlockSize;
            copyMatrix(pAMatrix, Size, pMatrixAblock, BlockSize, row_start, column_start, 0, 0, BlockSize, BlockSize);
            copyMatrix(pBMatrix, Size, pBblock, BlockSize, row_start, column_start, 0, 0, BlockSize, BlockSize);
            MPI_Send(pMatrixAblock, BlockSize * BlockSize, MPI_DOUBLE, ProcRec, 0, MPI_COMM_WORLD);
            MPI_Send(pBblock, BlockSize * BlockSize, MPI_DOUBLE, ProcRec, 0, MPI_COMM_WORLD);
        }
        copyMatrix(pAMatrix, Size, pMatrixAblock, BlockSize, 0, 0, 0, 0, BlockSize, BlockSize);
        copyMatrix(pBMatrix, Size, pBblock, BlockSize, 0, 0, 0, 0, BlockSize, BlockSize);
    }
    else {
        MPI_Status status;
        MPI_Recv(pMatrixAblock, BlockSize * BlockSize, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(pBblock, BlockSize * BlockSize, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
    }
}

void FoxProc::parallelResultCalculation() {
    for (int iter = 0; iter < GridSize; iter ++) {
        // Sending blocks of matrix A to the process grid rows
        ABlockCommunication (iter);
        // Block multiplication
        BlockMultiplication(pAblock, pBblock, pCblock, BlockSize);
        // Cyclic shift of blocks of matrix B in process grid columns
        BblockCommunication();
    }
}

void FoxProc::resultCollection(double *pCMatrix) {
    if (ProcRank == 0) {
        copyMatrix(pCblock, BlockSize, pCMatrix, Size, 0, 0, 0, 0, BlockSize, BlockSize);
        for (int ProcSender = 1; ProcSender < ProcNum; ProcSender++) {
            int row_start = (ProcSender / 2) * BlockSize, column_start = (ProcSender % 2) * BlockSize;
            MPI_Status status;
            MPI_Recv(pCblock, BlockSize * BlockSize, MPI_DOUBLE, ProcSender, 0, MPI_COMM_WORLD, &status);
            copyMatrix(pCblock, BlockSize, pCMatrix, Size, 0, 0, row_start, column_start, BlockSize, BlockSize);
        }
    }
    else {
        MPI_Send(pCblock, BlockSize * BlockSize, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
    }
    zeroFill(pCblock, BlockSize * BlockSize);
}

void FoxProc::createGridCommunicators() {
    int DimSize[2]; // Number of processes in each dimension of the grid
    int Periodic[2]; // =1, if the grid dimension should be periodic
    int Subdims[2]; // =1, if the grid dimension should be fixed
    DimSize[0] = GridSize;
    DimSize[1] = GridSize;
    Periodic[0] = 0;
    Periodic[1] = 0;
    // Creation of the Cartesian communicator
    std::cout << "Creating";
    MPI_Cart_create(MPI_COMM_WORLD, 2, DimSize, Periodic, 1, &GridComm);
    // Determination of the cartesian coordinates for every process
    MPI_Cart_coords(GridComm, ProcRank, 2, GridCoords);
    // Creating communicators for rows
    Subdims[0] = 0; // Dimensionality fixing
    Subdims[1] = 1; // The presence of the given dimension in the subgrid
    MPI_Cart_sub(GridComm, Subdims, &RowComm);
    // Creating communicators for columns
    Subdims[0] = 1;
    Subdims[1] = 0;
    MPI_Cart_sub(GridComm, Subdims, &ColComm);
}
