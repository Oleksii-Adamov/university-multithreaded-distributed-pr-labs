//
// Created by lvler on 15.10.2022.
//

#ifndef FOXALGOCPP_TAPEPROC_H
#define FOXALGOCPP_TAPEPROC_H


#include "Proc.h"

class TapeProc : public Proc {
private:
    double* pRows;
    double* pCols;
    double* pCRows;
    int nData; // number of rows/cols
    int Size;

    void colsCommunication();

public:
    explicit TapeProc(int Size);

    virtual ~TapeProc();

    void dataDistribution(double* pAMatrix, double* pBMatrix) override;

    void parallelResultCalculation() override;

    void resultCollection(double* pCMatrix) override;
};


#endif //FOXALGOCPP_TAPEPROC_H
