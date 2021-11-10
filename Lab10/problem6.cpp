#include <iostream>

using namespace std;

int main(){
    char str1[60], str2[60], output[110];
    cout << "write 1st word:\n";
    cin >> str1;
    cout << "write 2nd word:\n";
    cin >> str2;

    char * str_pointer = str1, * output_pointer = output;
    while(*str_pointer) *(output_pointer++) = *(str_pointer++);
    str_pointer = str2;
    while(*str_pointer) *(output_pointer++) = *(str_pointer++);
    *output_pointer = 0;

    cout << output;

    return 0;
}