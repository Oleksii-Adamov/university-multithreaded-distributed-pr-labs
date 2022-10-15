//
// Created by lvler on 15.10.2022.
//

#ifndef FOXALGOCPP_PROC_H
#define FOXALGOCPP_PROC_H


class Proc {
protected:
    int ProcNum; // Number of available processes
    int ProcRank; // Rank of current process
public:
    virtual void dataDistribution(double* pAMatrix, double* pBMatrix) = 0;

    virtual void parallelResultCalculation() = 0;

    virtual void resultCollection(double* pCMatrix) = 0;

    virtual ~Proc() {}
};


#endif //FOXALGOCPP_PROC_H
