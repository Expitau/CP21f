//
// Created by Jaeyun Kim on 2021/12/18.
//

#include "user_interface.h"
#include "view.h"
#include "frontend.h"

#include <sstream>

void UserInterface::run() {
    std::string command;
    std::string auth_info = auth_view->get_user_input("------ Authentication ------\n");
    if (frontend->auth(auth_info)) {
        main_view = post_view;
        do {
            command = post_view->get_user_input(
                    "-----------------------------------\n" +
                    frontend->get_user()->id + "@sns.com\n" +
                    "post : Post contents\n" +
                    "recommend <number> : recommend <number> interesting posts\n" +
                    "search <keyword> : List post entries whose contents contain <keyword>\n" +
                    "exit : Terminate this program\n" +
                    "-----------------------------------\n" +
                    "Command=");

        } while (query(command));
    }else{
        println("Failed Authentication.");
    }
}

bool UserInterface::query(std::string command) {
    std::vector<std::string> commandSlices;

    std::stringstream ss(command);
    std::string buf;
    while(!ss.eof()){
        ss >> buf;
        commandSlices.push_back(buf);
    }

    std::string instruction = commandSlices[0];

    if(instruction == "exit"){
        return false;
    }else if(instruction == "post"){
        post();
    }else if(instruction == "search"){
        search(commandSlices);
    }else if(instruction == "recommend"){
        recommend(std::stoi(commandSlices[1]));
    }else{
        // Illegal command foramt : comman
    }

    return true;
}

void UserInterface::create_ui(FrontEnd * frontend, std::istream& is, std::ostream& os){
    this->frontend = frontend;
    auth_view = new AuthView(is, os);
    post_view = new PostView(is, os);
    main_view = auth_view;
}

void UserInterface::post() {
    frontend->post(post_view->get_post("New Post"));
}

void UserInterface::search(std::vector<std::string> & command_vec) {
    frontend->search(command_vec);
}


void UserInterface::recommend(int number) {
    frontend->recommend(number);
}

void UserInterface::println(std::string obj) {
    main_view->template println(obj);
}

void UserInterface::print(std::string obj) {
    main_view->template print(obj);
}


