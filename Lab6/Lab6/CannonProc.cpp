//
// Created by lvler on 15.10.2022.
//

#include <iostream>
#include "CannonProc.h"
#include "util.h"

CannonProc::CannonProc(int Size, int GridSize) : BlockProc(Size, GridSize) {

}

CannonProc::~CannonProc() {

}

void CannonProc::ABlockCommunication() {
    MPI_Status Status;
    int NextProc = cyclic_pos(GridCoords[1] - 1, GridSize);
    int PrevProc = cyclic_pos(GridCoords[1] + 1, GridSize);
    MPI_Sendrecv_replace( pAblock, BlockSize*BlockSize, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, RowComm, &Status);
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
        if (iter < GridSize - 1) {
            // Cyclic shift of blocks of matrix A in process grid rows
            ABlockCommunication();
            // Cyclic shift of blocks of matrix B in process grid columns
            BblockCommunication();
        }
    }
}