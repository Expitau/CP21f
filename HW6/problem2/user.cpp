//
// Created by Jaeyun Kim on 2021/12/19.
//

#include "user.h"
#include <algorithm>
#include <string>

User::User(std::string id, std::string pw):id(id), pw(pw) {

}

bool User::auth(std::string pw_) {
    if(pw_.size() != pw.size()) return false;
    for(int i=0; i<pw.size(); i++){
        char a = pw[i], b = pw_[i];
        if('a' <= a && a <= 'z') a = a-'a'+'A';
        if('a' <= b && b <= 'z') b = b-'a'+'A';
        if(a != b) return false;
    }
    return true;
}
