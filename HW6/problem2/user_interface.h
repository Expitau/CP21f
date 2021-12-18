#ifndef PROBLEM2_USER_INTERFACE_H
#define PROBLEM2_USER_INTERFACE_H

#include <vector>
#include <algorithm>
#include "post.h"
#include "user.h"

class FrontEnd;
class View;
class PostView;
class AuthView;

class UserInterface {
private:
    FrontEnd * frontend;
    PostView * post_view;
    AuthView * auth_view;
    View * main_view;
public:
    void run();
    bool query(std::string command);
    void create_ui(FrontEnd * frontend, std::istream& is, std::ostream& os);
    void post();
    void search(std::vector<std::string>& command_vec);
    void recommend(int number);

    void println(std::string str);
    void print(std::string str);
};

#endif //PROBLEM2_USER_INTERFACE_H
