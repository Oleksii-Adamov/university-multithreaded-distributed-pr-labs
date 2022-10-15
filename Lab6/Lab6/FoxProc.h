//
// Created by lvler on 14.10.2022.
//

#ifndef FOXALGOCPP_FOXPROC_H
#define FOXALGOCPP_FOXPROC_H

#include "mpi.h"
#include "BlockProc.h"

class FoxProc : public BlockProc {
private:
    double* pMatrixAblock;

    void ABlockCommunication (int iter);

public:
    FoxProc(int Size, int GridSize);

    virtual ~FoxProc();

    void dataDistribution(double* pAMatrix, double* pBMatrix) override;

    void parallelResultCalculation() override;
};


#endif //FOXALGOCPP_FOXPROC_H
