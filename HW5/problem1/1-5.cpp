
bool bibimbap_change(int* bills, int N) {
    int change[3] = {0,0,0};

    for(int i=0; i<N; i++){
        switch(bills[i]){
        case 5:
            change[0] += 1;
            break;
        case 10:
            if(change[0] >= 1) change[0] -= 1; 
            else return false;
            change[1] += 1;
            break;
        case 20:
            if(change[1] >= 1 && change[0] >= 1) change[1] -= 1, change[0] -= 1;
            else if(change[0] >= 3) change[0] -= 3;
            else return false;
            change[2] += 1;
        }
    }

    return true;
}

