#ifndef PROBLEM2_BACKEND_H
#define PROBLEM2_BACKEND_H
#include <filesystem>
#include <fstream>
#include <string>
#include <vector>
#include <set>

#include "user.h"
#include "post.h"

class DataUtil{
private:
    static const std::filesystem::path DATA_DIRECTORY;
public:
    static std::filesystem::path get_user_path(std::string id);
    static std::filesystem::path get_user_post_path(std::string id);
    static std::vector<User*> get_all_users();
    static int get_max_id(std::filesystem::path file_path);
    static std::vector<std::string> get_file_content(std::filesystem::path file_path);

    static void select_recentN_post(std::vector<Post>& post_vec, int N);
    static void select_similarN_post(std::vector<PostSimilar>& post_vec, int N = 10);

    static void make_post(User * user, Post post);

    static std::vector<std::string> get_user_friends(std::string id);

    static Post parse_post(std::filesystem::path post_path);

    static std::vector<Post> get_user_post(std::string id);
    static std::vector<Post> get_user_recentN_post(std::string id, int N);

    static std::vector<PostSimilar> search_user_post(std::string id, const std::set<std::string> & keyword_set);
    static std::vector<PostSimilar> search_user_similarN_post(std::string id, const std::set<std::string> & keyword_set, int N = 10);
};



class BackEnd {
private:
    std::vector<User*> user_vec;
    int get_tot_max_id();
public:
    BackEnd();
    User* auth(std::string id, std::string pw);
    void post(User * user, Post post);

    std::vector<Post> recommend(User * user, int N);
    std::vector<Post> search(std::set<std::string> keyword_set);
};


#endif //PROBLEM2_BACKEND_H