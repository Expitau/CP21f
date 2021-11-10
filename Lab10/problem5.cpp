#include <iostream>

using namespace std;

void three_swap(int * a, int * b, int * c){
    int temp = *a;
    *a = *b;
    *b = *c;
    *c = temp;
}

void three_swap(int & a, int & b, int & c){
    int temp = a;
    a = b;
    b = c;
    c = temp;
}

int main(){
    int a, b, c;
    cin >> a >> b >> c;

    three_swap(a, b, c);
    cout << a << b << c << endl;

    three_swap(&a, &b, &c);
    cout << a << b << c << endl;

    return 0;
}