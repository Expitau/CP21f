#include <iostream>
#include <fstream>
using namespace std;

bool is_prime(int n){
    for(int i=2; i*i <= n; i++){
        if(!(n%i)) return false;
    }
    return true;
}

int main(){
    ifstream input("input.txt");
    ofstream output("output.txt");
    while(!input.eof()){
        int n;
        input>>n;
        output<< n << ":" << is_prime(n) << "\n";
    }
    return 0;
}