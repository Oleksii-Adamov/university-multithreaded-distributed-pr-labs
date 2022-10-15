//
// Created by lvler on 16.10.2022.
//

void seq_mult(double* pAMatrix, double* pBMatrix, double* pCMatrix, int Size) {
    for (int i = 0; i < Size; i++) {
        for (int j = 0; j < Size; j++) {
            double res = 0;
            for (int k = 0; k < Size; k++) {
                res += pAMatrix[i * Size + k] * pBMatrix[k * Size + j];
            }
            pCMatrix[i * Size + j] = res;
        }
    }
}
