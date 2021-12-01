
int* pascal_triangle(int N) {
    int * ret = new int[N]();
    ret[0] = 1;

    for(int i=0; i<N; i++) for(int j=i; j>0; j--) ret[j] += ret[j-1];
    return ret;
}

