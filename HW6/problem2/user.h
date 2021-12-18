//
// Created by Jaeyun Kim on 2021/12/19.
//

#ifndef PROBLEM2_USER_H
#define PROBLEM2_USER_H

#include <string>

class User {
private:
    std::string pw;
public:
    std::string id;
    User(std::string id, std::string pw);
    bool auth(std::string pw);
};


#endif //PROBLEM2_USER_H
