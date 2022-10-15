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

void copyCols(double* from, int from_num_rows, double* to, int from_column_start, int num_cols) {
    int to_index = 0;
    for (int j = from_column_start; j < from_column_start + num_cols; j++) {
        for (int i = 0; i < from_num_rows; i++, to_index++) {
            to[to_index] = from[i * from_num_rows + j];
//            std::cout << i << " " << j << " " << i * from_num_rows + j << " " << to_index << " " <<
//                      /*from[i * from_num_rows + j]*/to[to_index] << "\n";
        }
    }
//    std::cout << "\n";
}


void TapeProc::dataDistribution(double *pAMatrix, double *pBMatrix) {
    if (ProcRank == 0) {
        //std::cout << nData << "\n";
        for (int ProcRec = 1; ProcRec < ProcNum; ProcRec++) {
            int start = ProcRec * nData;
            copyMatrix(pAMatrix, Size, pRows, Size, start, 0, 0, 0, nData, Size);
            copyCols(pBMatrix, Size, pCols, start, nData);

            MPI_Send(pRows, Size * nData, MPI_DOUBLE, ProcRec, 0, MPI_COMM_WORLD);
            MPI_Send(pCols, Size * nData, MPI_DOUBLE, ProcRec, 1, MPI_COMM_WORLD);
        }
        copyMatrix(pAMatrix, Size, pRows, Size, 0, 0, 0, 0, nData, Size);
        copyCols(pBMatrix, Size, pCols, 0, nData);
        printMatrix(pRows, nData, Size);
        printMatrix(pCols, nData, Size);
    }
    else {
        MPI_Status status;
        MPI_Recv(pRows, Size * nData, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, &status);
        MPI_Recv(pCols, Size * nData, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD, &status);
    }
//    std::cout << ProcRank << "\n";
//    printMatrix(pRows, nData, Size);
//    printMatrix(pCols, nData, Size);
    MPI_Barrier(MPI_COMM_WORLD);
}

void TapeProc::parallelResultCalculation() {
    //std::cout << nData << "\n";
    int j_start = ProcRank * nData;
    for (int iter = 0; iter < ProcNum; iter++, j_start -= nData, j_start = cyclic_pos(j_start, Size)) {
        std::cout << ProcRank << " " << iter << "\n";
        for (int row_index = 0; row_index < nData; row_index++) {
            for (int col_index = 0; col_index < nData; col_index++) {
                double dot_product = 0;
                for (int i = 0; i < Size; i++) {
                    dot_product += pRows[row_index * Size + i] * pCols[col_index * Size + i];
                    //std::cout << pRows[row_index * nData + i] << " * " << pCols[col_index * nData + i] << " + ";
                }
                pCRows[row_index * Size + j_start + col_index] = dot_product;
                //std::cout << "\n";
                std::cout << row_index * Size + j_start + col_index << " " << dot_product << "\n";
            }
        }
        if (iter < ProcNum - 1) {
            colsCommunication();
            MPI_Barrier(MPI_COMM_WORLD);
        }
    }
    /*
    int j = ProcRank;
    for (int iter = 0; iter < Size; iter++, j++, j = cyclic_pos(j, Size)) {
        for (int data_index = 0; data_index < nData; data_index++) {
            double dot_product = 0;
            for (int i = 0; i < Size; i++) {
                dot_product += pRows[data_index * nData + i] * pCols[data_index * nData + i];
            }
            pCRows[data_index * nData + j] = dot_product;
        }
        if (j < Size - 1)
            colsCommunication();
    }
     */
}

void TapeProc::colsCommunication() {
    MPI_Status Status;
    int NextProc = cyclic_pos(ProcRank + 1, ProcNum);
    int PrevProc = cyclic_pos(ProcRank - 1, ProcNum);
    //std::cout << ProcRank << " -> " << NextProc << "\n";
    MPI_Sendrecv_replace(pCols, nData * Size, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, MPI_COMM_WORLD, &Status);
}

void TapeProc::resultCollection(double *pCMatrix) {
    printMatrix(pCRows, nData, Size);
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
