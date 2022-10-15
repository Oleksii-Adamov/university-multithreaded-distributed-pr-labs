//
// Created by lvler on 15.10.2022.
//

#include <iostream>
#include "TapeProc.h"
#include "mpi.h"
#include "util.h"

TapeProc::TapeProc(int Size) : Size(Size){
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    nData = Size / ProcNum;
    pRows = new double [Size * nData];
    pCols = new double [Size * nData];
    pCRows = new double [Size * nData];
}

TapeProc::~TapeProc() {
    delete[] pRows;
    delete[] pCols;
    delete[] pCRows;
}

void TapeProc::dataDistribution(double *pAMatrix, double *pBMatrix) {
    if (ProcRank == 0) {
        for (int ProcRec = 1; ProcRec < ProcNum; ProcRec++) {
            int start = ProcRec * nData;
            copyMatrix(pAMatrix, Size, pRows, Size, start, 0, 0, 0, nData, Size);
            copyCols(pBMatrix, Size, pCols, start, nData);

            MPI_Send(pRows, Size * nData, MPI_DOUBLE, ProcRec, 0, MPI_COMM_WORLD);
            MPI_Send(pCols, Size * nData, MPI_DOUBLE, ProcRec, 1, MPI_COMM_WORLD);
        }
        copyMatrix(pAMatrix, Size, pRows, Size, 0, 0, 0, 0, nData, Size);
        copyCols(pBMatrix, Size, pCols, 0, nData);
    }
    else {
        MPI_Status status;
        MPI_Recv(pRows, Size * nData, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(pCols, Size * nData, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD, &status);
    }
    MPI_Barrier(MPI_COMM_WORLD);
}

void TapeProc::parallelResultCalculation() {
    int j_start = ProcRank * nData;
    for (int iter = 0; iter < ProcNum; iter++, j_start -= nData, j_start = cyclic_pos(j_start, Size)) {
        for (int row_index = 0; row_index < nData; row_index++) {
            for (int col_index = 0; col_index < nData; col_index++) {
                double dot_product = 0;
                for (int i = 0; i < Size; i++) {
                    dot_product += pRows[row_index * Size + i] * pCols[col_index * Size + i];
                }
                pCRows[row_index * Size + j_start + col_index] = dot_product;
            }
        }
        if (iter < ProcNum - 1) {
            colsCommunication();
            MPI_Barrier(MPI_COMM_WORLD);
        }
    }
}

void TapeProc::colsCommunication() {
    MPI_Status Status;
    int NextProc = cyclic_pos(ProcRank + 1, ProcNum);
    int PrevProc = cyclic_pos(ProcRank - 1, ProcNum);
    MPI_Sendrecv_replace(pCols, nData * Size, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, MPI_COMM_WORLD, &Status);
}

void TapeProc::resultCollection(double *pCMatrix) {
    if (ProcRank == 0) {
        copyMatrix(pCRows, Size, pCMatrix, Size, 0, 0, 0, 0, nData, Size);
        for (int ProcSender = 1; ProcSender < ProcNum; ProcSender++) {
            int start = ProcSender * nData;
            MPI_Status status;
            MPI_Recv(pCRows, nData * Size, MPI_DOUBLE, ProcSender, 0, MPI_COMM_WORLD, &status);
            copyMatrix(pCRows, Size, pCMatrix, Size, 0, 0, start, 0, nData, Size);
        }
    }
    else {
        MPI_Send(pCRows, nData * Size, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
    }
}
