#ifndef FOXALGOCPP_UTIL_H
#define FOXALGOCPP_UTIL_H

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

#endif //FOXALGOCPP_UTIL_H
