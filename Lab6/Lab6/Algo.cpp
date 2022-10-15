//
// Created by lvler on 14.10.2022.
//

#include "Algo.h"
#include "FoxProc.h"
#include <cmath>
#include <iostream>
#include "util.h"
#include "CannonProc.h"
#include "TapeProc.h"
#include "seqmult.h"

Algo::Algo(AlgoType algoType) : algoType(algoType)
{
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    if (algoType == AlgoType::Tape) {
        GridSize = ProcNum;
    }
    else if (algoType == AlgoType::Seqential) {
        GridSize = 1;
    }
    else {
        GridSize = sqrt((double) ProcNum);
    }
    if (ProcRank == 0) {
        do {
            printf("\nEnter size of the initial objects: ");
            scanf("%d", &Size);
            if (Size % GridSize != 0) {
                printf ("Size of matricies must be divisible by the grid size! \n");
            }
        }
        while (Size % GridSize != 0);
    }
    MPI_Bcast(&Size, 1, MPI_INT, 0, MPI_COMM_WORLD);
    if ((algoType == AlgoType::Fox ||  algoType == AlgoType::Cannon) && ProcNum != GridSize*GridSize) {
        throw std::invalid_argument("Number of processes must be a perfect square");
    }
    if (ProcRank == 0) {
        pAMatrix = new double [Size*Size];
        pBMatrix = new double [Size*Size];
        pCMatrix = new double [Size*Size];
    }
    MPI_Barrier(MPI_COMM_WORLD);
}

Algo::~Algo() {
    if (ProcRank == 0) {
        delete[] pAMatrix;
        delete[] pBMatrix;
        delete[] pCMatrix;
    }
}

void Algo::execute(int times) {
    std::default_random_engine re;
    Proc* proc = nullptr;
    switch(algoType) {
        case AlgoType::Tape:
            proc = new TapeProc(Size);
            break;
        case AlgoType::Fox:
            proc = new FoxProc(Size, GridSize);
            break;
        case AlgoType::Cannon:
            proc = new CannonProc(Size, GridSize);
            break;
    }
    for (int t = 0; t < times; t++) {
        if (ProcRank == 0) {
            randomDataInitialization(pAMatrix, pBMatrix, Size, re);
        }
        MPI_Barrier(MPI_COMM_WORLD);
        double start_time, end_time;
        if (ProcRank == 0)
            start_time = MPI_Wtime();
        if (algoType == AlgoType::Seqential) {
            if (ProcRank == 0) {
                seq_mult(pAMatrix, pBMatrix, pCMatrix, Size);
            }
        }
        else {
            proc->dataDistribution(pAMatrix, pBMatrix);
            proc->parallelResultCalculation();
            proc->resultCollection(pCMatrix);
        }
        if (ProcRank == 0) {
            end_time = MPI_Wtime();
            mean_time += (end_time - start_time) / times;
            //correct = correct && isMultiplicationCorrect(pAMatrix, pBMatrix, pCMatrix, Size);
        }
    }
    delete proc;
}

bool Algo::isCorrect() {
    return correct;
}

double Algo::getMeanTime() {
    return mean_time;
}
