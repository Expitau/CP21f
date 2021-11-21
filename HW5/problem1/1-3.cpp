
void merge_arrays(int* arr1, int len1, int* arr2, int len2) {
    int len = len1 + len2;

    for(int i=len1-1; i>=0; i--) arr1[len2+i] = arr1[i], arr1[i] = 0;


    int p = 0, p1 = len2, p2 = 0;

    while(p1 < len && p2 < len2){
        if(arr1[p1] < arr2[p2]) arr1[p++] = arr1[p1++];
        else arr1[p++] = arr2[p2++];
    }

    while(p1 < len) arr1[p++] = arr1[p1++];
    while(p2 < len2) arr1[p++] = arr2[p2++];
}

