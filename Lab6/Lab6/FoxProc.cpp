//
// Created by lvler on 14.10.2022.
//

#include "FoxProc.h"
#include "util.h"

FoxProc::FoxProc(int Size, int GridSize) : BlockProc(Size, GridSize) {
    pMatrixAblock = new double [BlockSize*BlockSize];
}

FoxProc::~FoxProc() {
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

void FoxProc::dataDistribution(double *pAMatrix, double *pBMatrix) {
    if (ProcRank == 0) {
        for (int ProcRec = 1; ProcRec < ProcNum; ProcRec++) {
            int row_start = (ProcRec / GridSize) * BlockSize, column_start = (ProcRec % GridSize) * BlockSize;
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

        if (iter < GridSize - 1) {
            // Cyclic shift of blocks of matrix B in process grid columns
            BblockCommunication();
        }
    }
}
