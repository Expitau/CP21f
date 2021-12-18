//
// Created by Jaeyun Kim on 2021/12/19.
//

#include "user.h"

User::User(std::string id, std::string pw):id(id), pw(pw) {

}

bool User::auth(std::string pw) {
    return this->pw == pw;
}
