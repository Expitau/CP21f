#ifndef PROBLEM2_VIEW_H
#define PROBLEM2_VIEW_H

#include <istream>
#include <ostream>
#include <string>
#include "post.h"
#include "user.h"

class View {
public:
    std::istream & is;
    std::ostream & os;
    View(std::istream& is, std::ostream& os);
    virtual std::string get_user_input(std::string prompt);

    template <typename T>
    void println(T obj);

    template <typename T>
    void print(T obj);
};

class PostView : public View {
public:
    PostView(std::istream& is, std::ostream& os);
    std::pair<std::string, std::string> get_post(std::string prompt);

};

class AuthView : public View {
public:
    AuthView(std::istream& is, std::ostream& os);

    std::string get_user_input(std::string prompt) override;
};


#endif //PROBLEM2_VIEW_H
