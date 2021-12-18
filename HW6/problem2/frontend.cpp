//
// Created by Jaeyun Kim on 2021/12/18.
//

#include "frontend.h"
#include <sstream>
#include <set>

FrontEnd::FrontEnd(UserInterface & ui_, BackEnd & backend_):ui(ui_), backend(backend_) {
    user = nullptr;
}

FrontEnd::~FrontEnd(){
    if(user) delete user;
}

User* FrontEnd::get_user() {
    return user;
}

bool FrontEnd::auth(const std::string & authInfo) {
    std::string id, pw;
    std::stringstream ss(authInfo);
    ss >> id >> pw;

    user = backend.auth(id, pw);

    return user != nullptr;
}

void FrontEnd::post(const std::pair<std::string, std::string> & titleContentPair) {
    Post post(titleContentPair.first, titleContentPair.second);
    backend.post(user, post);
}

void FrontEnd::recommend(int N) {
    std::vector<Post> post_vec = backend.recommend(user, N);
    for(Post& post : post_vec){
        std::stringstream ss;
        ss << post;
        ui.println(ss.str());
    }
}

void FrontEnd::search(std::vector<std::string> keywords) {
    std::set<std::string> keyword_set;
    for(int i=1; i<keywords.size(); i++){
        keyword_set.insert(keywords[i]);
    }
    std::vector<Post> res = backend.search(keyword_set);

    ui.println("-----------------------------------");
    for(Post & post : res) ui.println(post.get_summary());
}



