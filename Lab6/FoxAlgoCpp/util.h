#ifndef FOXALGOCPP_UTIL_H
#define FOXALGOCPP_UTIL_H

#include <random>

void zeroFill(double* arr, int size);

void copyArr(double* from, double* to, int size);

void copyMatrix(double* from, int from_size, double* to, int to_size,
                int from_row_start, int from_column_start,
                int to_row_start, int to_column_start, int num_row, int num_col);

void copyCols(double* from, int from_num_rows, double* to, int from_column_start, int num_cols);

void randomArr(double* arr, int size, std::default_random_engine &re);

void randomDataInitialization(double* pAMatrix, double* pBMatrix, int Size, std::default_random_engine &re);

void BlockMultiplication (double *pAblock, double *pBblock, double *pCblock, int BlockSize);

void printMatrix(double* matrix, int num_rows, int num_cols);

bool isMultiplicationCorrect(double* pAMatrix, double* pBMatrix, double* pCMatrix, int Size);

int cyclic_pos(int pos, int size);

#endif //FOXALGOCPP_UTIL_H
