//
// Created by lvler on 15.10.2022.
//

#include <iostream>
#include "CannonProc.h"
#include "util.h"

CannonProc::CannonProc(int Size, int GridSize) : Size(Size), GridSize(GridSize) {
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    BlockSize = Size / GridSize;
    pAblock = new double [BlockSize*BlockSize];
    pBblock = new double [BlockSize*BlockSize];
    pCblock = new double [BlockSize*BlockSize];
    zeroFill(pCblock, BlockSize * BlockSize);
    createGridCommunicators();
}

CannonProc::~CannonProc() {
    delete[] pAblock;
    delete[] pBblock;
    delete[] pCblock;
}

void CannonProc::ABlockCommunication() {
    MPI_Status Status;
    int NextProc = cyclic_pos(GridCoords[1] - 1, GridSize);
    int PrevProc = cyclic_pos(GridCoords[1] + 1, GridSize);
    MPI_Sendrecv_replace( pAblock, BlockSize*BlockSize, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, RowComm, &Status);
}

void CannonProc::BblockCommunication() {
    MPI_Status Status;
    int NextProc = cyclic_pos(GridCoords[0] - 1, GridSize);
    int PrevProc = cyclic_pos(GridCoords[0] + 1, GridSize);
    MPI_Sendrecv_replace( pBblock, BlockSize*BlockSize, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, ColComm, &Status);
}

int CannonProc::procRankByCoord(int row, int col) {
    return row * GridSize + col;
}

void CannonProc::dataDistribution(double *pAMatrix, double *pBMatrix) {
    // initial (i,j) distribution
    if (ProcRank == 0) {
        for (int ProcRec = 1; ProcRec < ProcNum; ProcRec++) {
            int row_start = (ProcRec / GridSize) * BlockSize, column_start = (ProcRec % GridSize) * BlockSize;
            copyMatrix(pAMatrix, Size, pAblock, BlockSize, row_start, column_start, 0, 0, BlockSize, BlockSize);
            copyMatrix(pBMatrix, Size, pBblock, BlockSize, row_start, column_start, 0, 0, BlockSize, BlockSize);
            MPI_Send(pAblock, BlockSize * BlockSize, MPI_DOUBLE, ProcRec, 0, MPI_COMM_WORLD);
            MPI_Send(pBblock, BlockSize * BlockSize, MPI_DOUBLE, ProcRec, 0, MPI_COMM_WORLD);
        }
        copyMatrix(pAMatrix, Size, pAblock, BlockSize, 0, 0, 0, 0, BlockSize, BlockSize);
        copyMatrix(pBMatrix, Size, pBblock, BlockSize, 0, 0, 0, 0, BlockSize, BlockSize);
    }
    else {
        MPI_Status status;
        MPI_Recv(pAblock, BlockSize * BlockSize, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(pBblock, BlockSize * BlockSize, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
    }
    MPI_Barrier(MPI_COMM_WORLD);
    // Cannon initialization
    int AShift = GridCoords[0];
    int ASenderProc = cyclic_pos(GridCoords[1] + AShift, GridSize);
    int ARecProc = cyclic_pos(GridCoords[1] - AShift, GridSize);
    int BShift = GridCoords[1];
    int BSenderProc = cyclic_pos(GridCoords[0] + BShift, GridSize);
    int BRecProc = cyclic_pos(GridCoords[0] - BShift, GridSize);
    MPI_Status status;
    MPI_Sendrecv_replace(pAblock, BlockSize*BlockSize, MPI_DOUBLE, ARecProc, 0, ASenderProc, 0, RowComm, &status);
    MPI_Sendrecv_replace(pBblock, BlockSize*BlockSize, MPI_DOUBLE, BRecProc, 0, BSenderProc, 0, ColComm, &status);
}

void CannonProc::parallelResultCalculation() {
    for (int iter = 0; iter < GridSize; iter ++) {
        // Block multiplication
        BlockMultiplication(pAblock, pBblock, pCblock, BlockSize);
        // Cyclic shift of blocks of matrix A in process grid rows
        ABlockCommunication();
        // Cyclic shift of blocks of matrix B in process grid columns
        BblockCommunication();
    }
}

void CannonProc::resultCollection(double *pCMatrix) {
    if (ProcRank == 0) {
        copyMatrix(pCblock, BlockSize, pCMatrix, Size, 0, 0, 0, 0, BlockSize, BlockSize);
        for (int ProcSender = 1; ProcSender < ProcNum; ProcSender++) {
            int row_start = (ProcSender / GridSize) * BlockSize, column_start = (ProcSender % GridSize) * BlockSize;
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

void CannonProc::createGridCommunicators() {
    int DimSize[2]; // Number of processes in each dimension of the grid
    int Periodic[2]; // =1, if the grid dimension should be periodic
    int Subdims[2]; // =1, if the grid dimension should be fixed
    DimSize[0] = GridSize;
    DimSize[1] = GridSize;
    Periodic[0] = 0;
    Periodic[1] = 0;
    // Creation of the Cartesian communicator
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