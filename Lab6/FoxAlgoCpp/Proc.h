//
// Created by lvler on 15.10.2022.
//

#ifndef FOXALGOCPP_PROC_H
#define FOXALGOCPP_PROC_H


class Proc {
public:
    virtual void dataDistribution(double* pAMatrix, double* pBMatrix) = 0;

    virtual void parallelResultCalculation() = 0;

    virtual void resultCollection(double* pCMatrix) = 0;
};


#endif //FOXALGOCPP_PROC_H
