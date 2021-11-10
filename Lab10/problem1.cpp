#include <iostream>
#include <string>

using namespace std;

int main() {
    const string prof_name = "Youngki";
    string input_name;

    cin >> input_name;
    if(prof_name == input_name) cout << "Hello, Professor!";
    else cout << "Hello, " << input_name << "!";
    return 0;
}
