#ifndef PROBLEM2_POST_H
#define PROBLEM2_POST_H

#include <string>
#include <ctime>
#include <ostream>
#include <sstream>
#include <set>


class Post {
protected:
    int id;
    static const int ID_NOT_INITIATED = -1;
    std::string title, content;
    std::time_t date_time;

public:
    Post(std::string title, std::string content);
    Post(int id, std::time_t dateTime, std::string title, std::string content);

    std::string get_summary();

    friend std::ostream& operator << (std::ostream & os, Post const& post);

    int get_id();
    void set_id(int id);

    std::string get_date() const;
    void set_date_time(std::time_t date_time);

    std::string get_title();
    std::string get_content();

    static std::time_t parse_time(std::string time_str);
};


class PostSimilar{
public:
    Post post;
    int same_cnt, tot_cnt;

    PostSimilar(Post post, const std::set<std::string> keyword_set);

    void calculate_cnt(const std::set<std::string> & keyword_set);
};


#endif //PROBLEM2_POST_H
