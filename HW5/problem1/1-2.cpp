
int hamming_distance(int x, int y) {
    int ret = 0;
    for(int t = x^y; t; t>>=1) ret += t&1;
    return ret;
}

