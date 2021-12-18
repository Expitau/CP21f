//
// Created by Jaeyun Kim on 2021/12/18.
//

#include "backend.h"
#include <ostream>
#include <istream>
#include <fstream>
#include <algorithm>

const std::filesystem::path DataUtil::DATA_DIRECTORY("./data");


std::filesystem::path DataUtil::get_user_path(std::string id){
    return std::filesystem::path(DATA_DIRECTORY / id);
}

std::filesystem::path DataUtil::get_user_post_path(std::string id){
    return get_user_path(id) / "post";
}

std::vector<std::string> DataUtil::get_user_friends(std::string id){
    std::filesystem::path friend_path = get_user_path(id) / "friend.txt";
    return get_file_content(friend_path);
}

std::vector<std::string> DataUtil::get_file_content(std::filesystem::path file_path){
    std::ifstream ifs(file_path);
    std::vector<std::string> ret;
    std::string tmp;
    while(!ifs.eof()){
        std::getline(ifs, tmp);
        ret.push_back(tmp);
    }
    return ret;
}

std::vector<User*> DataUtil::get_all_users() {
    std::vector<User*> user_vec;

    for(auto const & dir_entry : std::filesystem::directory_iterator(DATA_DIRECTORY)){
        if(dir_entry.is_directory()){
            std::filesystem::path path = dir_entry.path();
            std::filesystem::path pw_path = path / "password.txt";

            std::string name = path.filename();
            std::string pw = get_file_content(pw_path)[0]; // TODO: possible error check

            user_vec.push_back(new User(name, pw));
        }
    }

    return user_vec;
}

int DataUtil::get_max_id(std::filesystem::path file_path) {
    if(!exists(file_path) || file_path.empty()) return -1;

    int res = -1;
    for(auto const & dir_entry : std::filesystem::directory_iterator(file_path)) {
        int id = std::stoi(dir_entry.path().stem()); // TODO: possible error check
        if(id > res) res = id;
    }

    return res;
}

void DataUtil::make_post(User * user, Post post) {
    std::filesystem::path post_path = get_user_post_path(user->id) / (std::to_string(post.get_id()) + ".txt");
    std::ofstream output(post_path);

    output << post.get_date() << std::endl;
    output << post.get_title() << std::endl;
    output << std::endl;
    output << post.get_content();
    output.flush();
}

void DataUtil::select_recentN_post(std::vector<Post> & post_vec, int N) {
    std::sort(post_vec.begin(), post_vec.end(),
              [](const Post &p1, const Post &p2) {
                  return p1.get_date() > p2.get_date();
              });
    if (post_vec.size() > N) {
        post_vec.erase(post_vec.begin() + N, post_vec.end());
    }
}

void DataUtil::select_similarN_post(std::vector<PostSimilar>& post_vec, int N){
    std::sort(post_vec.begin(), post_vec.end(),
              [](const PostSimilar &p1, const PostSimilar &p2) {
                  if(p1.same_cnt != p2.same_cnt) return p1.same_cnt > p2.same_cnt;
                  return p1.tot_cnt > p2.tot_cnt;
              });
    if (post_vec.size() > N) {
        post_vec.erase(post_vec.begin() + N, post_vec.end());
    }
}

Post DataUtil::parse_post(std::filesystem::path post_path) {
    int id = std::stoi(post_path.stem());
    std::vector<std::string> data = get_file_content(post_path);

    std::time_t time = Post::parse_time(data[0]);
    std::string title = data[1];
    std::string content;

    for(int i=2; i<data.size(); i++) content += data[i] + "\n";

    return Post(id, time, title, content);
}

std::vector<Post> DataUtil::get_user_post(std::string id) {
    std::filesystem::path post_path = get_user_post_path(id);
    std::vector<Post> post_vec;

    for(auto const & dir_entry : std::filesystem::directory_iterator(post_path)) {
        post_vec.push_back(parse_post(dir_entry.path()));
    }

    return post_vec;
}

std::vector<Post> DataUtil::get_user_recentN_post(std::string id, int N) {
    std::vector<Post> post_vec = get_user_post(id);
    select_recentN_post(post_vec, N);
    return post_vec;
}

std::vector<PostSimilar> DataUtil::search_user_post(std::string id, const std::set<std::string> & keyword_set) {
    std::vector<PostSimilar> ret;
    for(Post post : get_user_post(id)){
        PostSimilar ps(post, keyword_set);
        if(ps.same_cnt != 0) ret.push_back(ps);
    }

    return ret;
}

std::vector<PostSimilar>
DataUtil::search_user_similarN_post(std::string id, const std::set<std::string> &keyword_set, int N) {
    std::vector<PostSimilar> ret = search_user_post(id, keyword_set);
    select_similarN_post(ret, N);
    return ret;
}

User* BackEnd::auth(std::string id, std::string pw) {
    for(User* user : user_vec){
        if(user->id == id && user->auth(pw)){
            return user;
        }
    }
    return nullptr;
}

BackEnd::BackEnd() {
    user_vec = DataUtil::get_all_users();
}

int BackEnd::get_tot_max_id() {
    int res = -1;
    for(User* user : user_vec){
        int ret = DataUtil::get_max_id(DataUtil::get_user_post_path(user->id));
        if(ret > res) res = ret;
    }
    return res;
}

void BackEnd::post(User * user, Post post) {
    post.set_id(get_tot_max_id() + 1);
    DataUtil::make_post(user, post);
}

std::vector<Post> BackEnd::recommend(User * user, int N) {
    std::vector<std::string> friend_vec = DataUtil::get_user_friends(user->id);

    std::vector<Post> post_vec;

    for(std::string id : friend_vec){
        for(auto post : DataUtil::get_user_recentN_post(id, N)){
            post_vec.push_back(post);
        }
    }

    DataUtil::select_recentN_post(post_vec, N);

    return post_vec;
}

std::vector<Post> BackEnd::search(std::set<std::string> keyword_set){
    std::vector<PostSimilar> posts;
    for(User * user : user_vec){
        for(PostSimilar post : DataUtil::search_user_similarN_post(user->id, keyword_set)){
            posts.push_back(post);
        }
    }
    DataUtil::select_similarN_post(posts);

    std::vector<Post> ret;
    for(PostSimilar post : posts) ret.push_back(post.post);

    return ret;
}