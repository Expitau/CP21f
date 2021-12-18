#ifndef PROBLEM2_FRONTEND_H
#define PROBLEM2_FRONTEND_H

#include <string>

#include "backend.h"
#include "user_interface.h"
#include "user.h"
#include "post.h"

class FrontEnd {
private:
    UserInterface & ui;
    BackEnd & backend;
    User* user;

public:
    FrontEnd(UserInterface & ui_, BackEnd & backend_);
    ~FrontEnd();
    bool auth(const std::string & authInfo);
    void post(const std::pair<std::string, std::string> & titleContentPair);
    void recommend(int N);
    void search(std::vector<std::string> keywords);

    User* get_user();

};


#endif //PROBLEM2_FRONTEND_H
