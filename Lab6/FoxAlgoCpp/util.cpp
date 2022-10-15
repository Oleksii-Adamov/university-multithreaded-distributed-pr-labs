#include <iostream>
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

void copyMatrix(double* from, int from_num_cols, double* to, int to_num_cols,
                int from_row_start, int from_column_start,
                int to_row_start, int to_column_start, int num_row, int num_col) {
    //std::cout << to_num_rows << "\n";
    for (int i_from = from_row_start, i_to = to_row_start; i_to < to_row_start + num_row; i_from++, i_to++) {
        for (int j_from = from_column_start, j_to = to_column_start; j_to < to_column_start + num_col; j_from++, j_to++) {
            //std::cout << i_to << " " << j_to << " " << i_to * to_num_cols + j_to << "\n";
            to[i_to * to_num_cols + j_to] = from[i_from * from_num_cols + j_from];
        }
    }
}

void randomArr(double* arr, int size, std::default_random_engine &re) {
    std::uniform_real_distribution<double> unif(-100.0,100.0);
    std::uniform_int_distribution<int> unif_int(1, 9);
    for (int i = 0; i < size; i++) {
        //arr[i] = unif(re);
        arr[i] = unif_int(re);
    }
}

void randomDataInitialization(double* pAMatrix, double* pBMatrix, int Size, std::default_random_engine &re) {
    randomArr(pAMatrix, Size * Size, re);
    randomArr(pBMatrix, Size * Size, re);
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

void printMatrix(double* matrix, int num_rows, int num_cols) {
    for (int i = 0; i < num_rows; i++) {
        for (int j = 0; j < num_cols; j++) {
            std::cout << matrix[i*num_cols + j] << " ";
        }
        std::cout << std::endl;
    }
    std::cout << std::endl;
}

bool isMultiplicationCorrect(double* pAMatrix, double* pBMatrix, double* pCMatrix, int Size) {
    bool correct = true;
    double eps = 1e-5;
    std::cout << "Expected: \n";
    for (int i = 0; i < Size; i++) {
        for (int j = 0; j < Size; j++) {
            double expected = 0;
            for (int k = 0; k < Size; k++)
                expected += pAMatrix[i * Size + k] * pBMatrix[k * Size + j];
            if (abs(expected - pCMatrix[i * Size + j]) > eps) {
                correct = false;
            }
            std::cout << expected << " ";
        }
        std::cout << "\n";
    }
    std::cout << "\n";
    return correct;
}

int cyclic_pos(int pos, int size) {
    while (pos < 0) {
        pos += size;
    }
    while (pos > size - 1) {
        pos -= size;
    }
    return pos;
}