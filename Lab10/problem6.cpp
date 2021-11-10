#include <iostream>

using namespace std;

int main(){
    char str1[110], str2[60];
    cout << "write 1st word:\n";
    cin >> str1;
    cout << "write 2nd word:\n";
    cin >> str2;

    char * str1_pointer = str1, * str2_pointer = str2;
    while(*str1_pointer) str1_pointer++;
    while(*str2_pointer) *(str1_pointer++) = *(str2_pointer++);
    *str1_pointer = 0;

    cout << str1;

    return 0;
}