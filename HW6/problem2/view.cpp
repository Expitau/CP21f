//
// Created by Jaeyun Kim on 2021/12/18.
//

#include "view.h"

View::View(std::istream &is, std::ostream &os):is(is), os(os){

}

std::string View::get_user_input(std::string prompt) {
    print<std::string>(prompt);
    try {
        std::string next_line;
        std::getline(is, next_line);

        return next_line;
    } catch (int n) {
        return "exit";
    }
}

template<typename T>
void View::println(T obj) {
    os << obj << std::endl;
}

template<typename T>
void View::print(T obj) {
    os << obj;
    os.flush();
}

PostView::PostView(std::istream &is, std::ostream &os) : View(is, os) {}

std::pair<std::string, std::string> PostView::get_post(std::string prompt) {
    std::string title;
    std::string content;
    std::string entireContent = "";
    println<std::string>("-----------------------------------");
    println<std::string>(prompt);
    print<std::string>("* Title=");

    std::getline(is, title);

    println<std::string>("* Content");
    print<std::string>("> ");

    std::getline(is, content);

    entireContent += content + "\n";

    while(!content.empty()){
        print<std::string>("> ");
        try {
            std::getline(is, content);
        } catch (int n) {
            content = "";
        }
        entireContent += content + "\n";
    }

    return std::pair<std::string, std::string>(title,entireContent);
}

AuthView::AuthView(std::istream &is, std::ostream &os) : View(is, os) {}

std::string AuthView::get_user_input(std::string prompt) {
    std::string id, passwd;

    print<std::string>(prompt);
    print<std::string>("id=");

    try {
        std::getline(is, id);
    } catch (int n) {
        id = "";
    }

    print<std::string>("passwd=");

    try {
        std::getline(is, passwd);
    } catch (int n) {
        passwd = "";
    }

    return id + "\n" + passwd;
}
