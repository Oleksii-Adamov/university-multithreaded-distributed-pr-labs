#include "util.h"

void zeroFill(double* arr, int size) {
    for (int i = 0; i < size; i++) {
        arr[i] = 0.0;
    }
}

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

void randomArr(double* arr, int size) {
    std::uniform_real_distribution<double> unif(-100.0,100.0);
    std::default_random_engine re;
    for (int i = 0; i < size; i++) {
        arr[i] = unif(re);
    }
}

void randomDataInitialization(double* pAMatrix, double* pBMatrix, int Size) {
    randomArr(pAMatrix, Size * Size);
    randomArr(pBMatrix, Size * Size);
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

bool isMultiplicationCorrect(double* pAMatrix, double* pBMatrix, double* pCMatrix, int Size) {
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
    return correct;
}