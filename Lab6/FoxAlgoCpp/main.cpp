//#include <stdio.h>
//#include <stdlib.h>
#include "mpi.h"
#include "math.h"
#include <random>
#include <iostream>

int ProcNum = 0; // Number of available processes
int ProcRank = 0; // Rank of current process
int GridSize; // Size of virtual processor grid
int GridCoords[2]; // Coordinates of current processor in grid
MPI_Comm GridComm; // Grid communicator
MPI_Comm ColComm; // Column communicator
MPI_Comm RowComm; // Row communicator

void copyArr(double* from, double* to, int size) {
    for (int i = 0; i < size; i++) {
        to[i] = from[i];
    }
}

void copyMatrix(double* from, int from_size, double* to, int to_size,
                int from_row_start, int from_column_start,
                int to_row_start, int to_column_start, int num_row, int num_col) {
    for (int i_from = from_row_start, i_to = to_row_start; i_to < to_row_start + num_row; i_from++, i_to++) {
        for (int j_from = from_column_start, j_to = to_column_start; j_to < to_column_start + num_col; j_from++, j_to++) {
            to[i_to * to_size + j_to] = from[i_from * from_size + j_from];
        }
    }
}

// Creation of two-dimensional grid communicator
// and communicators for each row and each column of the grid
void CreateGridCommunicators() {
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

void RandomArr(double* arr, int size) {
    std::uniform_real_distribution<double> unif(-100.0,100.0);
    std::default_random_engine re;
    for (int i = 0; i < size; i++) {
        arr[i] = unif(re);
    }
}

void RandomDataInitialization(double* pAMatrix, double* pBMatrix, int Size) {
    RandomArr(pAMatrix, Size * Size);
    RandomArr(pBMatrix, Size * Size);
}

void zeroFill(double* arr, int size) {
    for (int i = 0; i < size; i++) {
        arr[i] = 0.0;
    }
}

// Function for memory allocation and data initialization
void ProcessInitialization (double* &pAMatrix, double* &pBMatrix,
                            double* &pCMatrix, double* &pAblock, double* &pBblock, double* &pCblock,
                            double* &pTemporaryAblock, int &Size, int &BlockSize) {
    if (ProcRank == 0) {
        do {
            printf("\nEnter size of the initial objects: ");
            scanf("%d", &Size);
            if (Size%GridSize != 0) {
                printf ("Size of matricies must be divisible by the grid size! \n");
            }
        }
        while (Size%GridSize != 0);
    }
    MPI_Bcast(&Size, 1, MPI_INT, 0, MPI_COMM_WORLD);
    BlockSize = Size/GridSize;
    pAblock = new double [BlockSize*BlockSize];
    pBblock = new double [BlockSize*BlockSize];
    pCblock = new double [BlockSize*BlockSize];
    pTemporaryAblock = new double [BlockSize*BlockSize];
    zeroFill(pCblock, BlockSize * BlockSize);
    if (ProcRank == 0) {
        pAMatrix = new double [Size*Size];
        pBMatrix = new double [Size*Size];
        pCMatrix = new double [Size*Size];
        RandomDataInitialization(pAMatrix, pBMatrix, Size);
    }
    MPI_Barrier(MPI_COMM_WORLD);
}

void DataDistribution(double* pAMatrix, double* pBMatrix, double* pMatrixAblock, double* pBblock, int Size, int BlockSize) {
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

// Broadcasting matrix A blocks to process grid rows
void ABlockCommunication (int iter, double *pAblock, double* pMatrixAblock, int BlockSize) {
// Defining the leading process of the process grid row
    int Pivot = (GridCoords[0] + iter) % GridSize;
// Copying the transmitted block in a separate memory buffer
    if (GridCoords[1] == Pivot) {
        copyArr(pMatrixAblock, pAblock, BlockSize * BlockSize);
    }
// Block broadcasting
    MPI_Bcast(pAblock, BlockSize * BlockSize, MPI_DOUBLE, Pivot, RowComm);
}

void BlockMultiplication (double *pAblock, double *pBblock, double *pCblock, int BlockSize) {
    for (int i=0; i<BlockSize; i++) {
        for (int j=0; j<BlockSize; j++) {
            double temp = 0;
            for (int k=0; k<BlockSize; k++)
                temp += pAblock [i*BlockSize + k] * pBblock [k*BlockSize + j];
            pCblock [i*BlockSize + j] += temp;
        }
    }
}

// Cyclic shift of matrix B blocks in the process grid columns
void BblockCommunication (double *pBblock, int BlockSize) {
    MPI_Status Status;
    int NextProc = GridCoords[0] + 1;
    if ( GridCoords[0] == GridSize-1 ) NextProc = 0;
    int PrevProc = GridCoords[0] - 1;
    if ( GridCoords[0] == 0 ) PrevProc = GridSize-1;
    MPI_Sendrecv_replace( pBblock, BlockSize*BlockSize, MPI_DOUBLE,
                          NextProc, 0, PrevProc, 0, ColComm, &Status);
}

void ParallelResultCalculation(double* pAblock, double* pMatrixAblock,
                               double* pBblock, double* pCblock, int BlockSize) {
    for (int iter = 0; iter < GridSize; iter ++) {
// Sending blocks of matrix A to the process grid rows
        ABlockCommunication (iter, pAblock, pMatrixAblock, BlockSize);
// Block multiplication
        BlockMultiplication(pAblock, pBblock, pCblock, BlockSize);
// Cyclic shift of blocks of matrix B in process grid columns
        BblockCommunication(pBblock, BlockSize);
    }
}

void ResultCollection(double* pCMatrix, double* pCblock, int Size, int BlockSize) {
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

void TestResult(double* pAMatrix, double* pBMatrix, double* pCMatrix, int Size) {
    if (ProcRank == 0) {
        bool correct = true;
        double eps = 1e-5;
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                double expected = 0;
                for (int k = 0; k < Size; k++)
                    expected += pAMatrix[i * Size + k] * pBMatrix[k * Size + j];
                if (abs(expected - pCMatrix[i * Size + j]) > eps) {
                    correct = false;
                }
            }
        }
        if (correct) {
            std::cout << "Correct!" << std::endl;
        } else {
            std::cout << "Wrong!" << std::endl;
        }
    }
}

void ProcessTermination(double* pAMatrix, double* pBMatrix, double* pCMatrix, double* pAblock, double* pBblock, double* pCblock,
                        double* pMatrixAblock) {
    delete[] pAblock;
    delete[] pBblock;
    delete[] pCblock;
    delete[] pMatrixAblock;
    if (ProcRank == 0) {
        delete[] pAMatrix;
        delete[] pBMatrix;
        delete[] pCMatrix;
    }
}

int main(int argc, char * argv[]) {
    double* pAMatrix; // The first argument of matrix multiplication
    double* pBMatrix; // The second argument of matrix multiplication
    double* pCMatrix; // The result matrix
    int Size; // Size of matricies
    int BlockSize; // Sizes of matrix blocks on current process
    double *pAblock; // Initial block of matrix A on current process
    double *pBblock; // Initial block of matrix B on current process
    double *pCblock; // Block of result matrix C on current process
    double *pMatrixAblock;
    double Start, Finish, Duration;
    setvbuf(stdout, 0, _IONBF, 0);
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);
    GridSize = sqrt((double) ProcNum);
    if (ProcNum != GridSize*GridSize) {
        if (ProcRank == 0) {
            printf ("Number of processes must be a perfect square\n");
        }
    }
    else {
        if (ProcRank == 0)
            printf("Parallel matrix multiplication program\n");
        // Creating the cartesian grid, row and column communcators
        CreateGridCommunicators();
        // Memory allocation and initialization of matrix elements
        ProcessInitialization(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock,
                              pCblock, pMatrixAblock, Size, BlockSize);
//        if (ProcRank == 0)
//            std::cout << "Initialized" << std::endl;
        //std::cout << ProcRank << " " << i << std::endl;
        DataDistribution(pAMatrix, pBMatrix, pMatrixAblock, pBblock, Size,
                         BlockSize);
        //std::cout << ProcRank << " " << i << " Distributed" << std::endl;
        // Execution of Fox method
        ParallelResultCalculation(pAblock, pMatrixAblock, pBblock, pCblock, BlockSize);
        //std::cout << ProcRank << " " << i << " Computed" << std::endl;
        ResultCollection(pCMatrix, pCblock, Size, BlockSize);
        //std::cout << ProcRank << " " << i << " Collected" << std::endl;
        MPI_Barrier(MPI_COMM_WORLD);
        TestResult(pAMatrix, pBMatrix, pCMatrix, Size);
// Process Termination
        ProcessTermination(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock,
                           pCblock, pMatrixAblock);
    }
    MPI_Finalize();
    return 0;
}