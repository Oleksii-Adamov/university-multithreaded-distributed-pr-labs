//
// Created by lvler on 15.10.2022.
//

#ifndef FOXALGOCPP_CANNONPROC_H
#define FOXALGOCPP_CANNONPROC_H
#include "mpi.h"
#include "BlockProc.h"

class CannonProc : public BlockProc {
private:
    void ABlockCommunication();

public:
    CannonProc(int Size, int GridSize);

    virtual ~CannonProc();

    void dataDistribution(double* pAMatrix, double* pBMatrix) override;

    void parallelResultCalculation() override;
};


#endif //FOXALGOCPP_CANNONPROC_H
