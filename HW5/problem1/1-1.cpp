#include <string>

bool is_palindrome(std::string s) {
    int len = s.size();

    for(int i=0; i < len-i-1; i++){
        if(s[i] != s[len-i-1]) return false;
    }

    return true;
}