//
// Created by lvler on 15.10.2022.
//

#include <iostream>
#include "BlockProc.h"
#include "util.h"

BlockProc::BlockProc(int Size, int GridSize) : Size(Size), GridSize(GridSize) {
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    BlockSize = Size / GridSize;
    pAblock = new double [BlockSize*BlockSize];
    pBblock = new double [BlockSize*BlockSize];
    pCblock = new double [BlockSize*BlockSize];
    zeroFill(pCblock, BlockSize * BlockSize);
    createGridCommunicators();
}

BlockProc::~BlockProc() {
    delete[] pAblock;
    delete[] pBblock;
    delete[] pCblock;
}

void BlockProc::createGridCommunicators() {
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

void BlockProc::BblockCommunication() {
    MPI_Status Status;
    int NextProc = cyclic_pos(GridCoords[0] - 1, GridSize);
    int PrevProc = cyclic_pos(GridCoords[0] + 1, GridSize);
    MPI_Sendrecv_replace( pBblock, BlockSize*BlockSize, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, ColComm, &Status);
}

void BlockProc::resultCollection(double *pCMatrix) {
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
    MPI_Barrier(MPI_COMM_WORLD);
}
