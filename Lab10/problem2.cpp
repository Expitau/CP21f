#include <iostream>

#define PI 3.14159
#define AREA(r) (PI*(r)*(r))

using namespace std;

int main(){
    double r;
    cin >> r;
    cout << AREA(r);
    return 0;
}