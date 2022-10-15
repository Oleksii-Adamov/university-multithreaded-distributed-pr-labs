//
// Created by lvler on 14.10.2022.
//

#include "FoxAlgo.h"
#include "FoxProc.h"
#include <cmath>
#include "util.h"

FoxAlgo::FoxAlgo()
{
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    GridSize = sqrt((double) ProcNum);
    if (ProcRank == 0) {
        do {
            printf("\nEnter size of the initial objects: ");
            scanf("%d", &Size);
            if (Size % GridSize != 0) {
                printf ("Size of matricies must be divisible by the grid size! \n");
            }
        }
        while (Size%GridSize != 0);
    }
    MPI_Bcast(&Size, 1, MPI_INT, 0, MPI_COMM_WORLD);
    if (ProcNum != GridSize*GridSize) {
        throw std::invalid_argument("Number of processes must be a perfect square");
    }
    if (ProcRank == 0) {
        pAMatrix = new double [Size*Size];
        pBMatrix = new double [Size*Size];
        pCMatrix = new double [Size*Size];
    }
    MPI_Barrier(MPI_COMM_WORLD);
}

FoxAlgo::~FoxAlgo() {
    if (ProcRank == 0) {
        delete[] pAMatrix;
        delete[] pBMatrix;
        delete[] pCMatrix;
    }
}

void FoxAlgo::execute(int times) {
    FoxProc foxProc(Size, GridSize);
    for (int t = 0; t < times; t++) {
        if (ProcRank == 0)
            randomDataInitialization(pAMatrix, pBMatrix, Size);
        double start_time, end_time;
        if (ProcRank == 0)
            start_time = MPI_Wtime();
        foxProc.dataDistribution(pAMatrix, pBMatrix);
        // Execution of Fox method
        foxProc.parallelResultCalculation();
        foxProc.resultCollection(pCMatrix);
        MPI_Barrier(MPI_COMM_WORLD);
        if (ProcRank == 0) {
            end_time = MPI_Wtime();
            mean_time += (end_time - start_time) / times;
            correct = correct && isMultiplicationCorrect(pAMatrix, pBMatrix, pCMatrix, Size);
        }
    }
}

bool FoxAlgo::isCorrect() {
    return correct;
}

double FoxAlgo::getMeanTime() {
    return mean_time;
}