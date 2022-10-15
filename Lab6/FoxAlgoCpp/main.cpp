#include "mpi.h"
#include <iostream>
#include "FoxAlgo.h"

int main(int argc, char * argv[]) {
    int ProcNum = 0; // Number of available processes
    int ProcRank = 0; // Rank of current process
    int times = 100;
    setvbuf(stdout, 0, _IONBF, 0);
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &ProcNum);
    MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);

    FoxAlgo foxAlgo;
    foxAlgo.execute(times);
    if (ProcRank == 0) {
        if (foxAlgo.isCorrect()) {
            std::cout << "Correct!" << std::endl;
            std::cout << "Mean time = " << foxAlgo.getMeanTime() << " s" << std::endl;
        } else {
            std::cout << "Wrong!" << std::endl;
        }
    }

    MPI_Finalize();
    return 0;
}