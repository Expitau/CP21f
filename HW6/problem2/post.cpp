//
// Created by Jaeyun Kim on 2021/12/18.
//

#include <iomanip>
#include "post.h"

Post::Post(std::string title, std::string content):Post(ID_NOT_INITIATED, std::time(nullptr), title, content){}

Post::Post(int id_, std::time_t date_time_, std::string title_, std::string content_):id(id_), date_time(date_time_), title(title_), content(content_) {
    // content trim!!!!!
    content = content.erase(content.find_last_not_of(" \n\r\t")+1);
}

std::string Post::get_summary() {
    std::stringstream buf;
    buf << "id: " << id << ", created at: " << get_date() << ", title: " << title;
    buf.flush();
    return buf.str();
}

std::ostream &operator<<(std::ostream &os, const Post &post) {
    os << "-----------------------------------\n";
    os << "id: " << post.id << "\n";
    os << "created at: " << post.get_date() << "\n";
    os << "title: " << post.title << "\n";
    os << "content: " << post.content;
    os.flush();
    return os;
}

int Post::get_id() {
    return id;
}

void Post::set_id(int id) {
    this->id = id;
}

std::string Post::get_date() const {
    //TODO !!!!!!!!
    char buf[30];
    std::strftime(buf, 30, "%Y/%m/%d %T", localtime(&date_time));
    return std::string(buf);
}

void Post::set_date_time(std::time_t date_time) {
    this->date_time = date_time;
}

std::string Post::get_title() {
    return title;
}

std::string Post::get_content() {
    return content;
}

std::time_t Post::parse_time(std::string time_str) {
    std::stringstream ss(time_str);
    std::tm tm = {};
    ss >> std::get_time(&tm,  "%Y/%m/%d %T");
    return std::mktime(&tm);
}

void PostSimilar::calculate_cnt(const std::set<std::string> & keyword_set) {
    same_cnt = 0;
    tot_cnt = 0;
    std::stringstream title_ss(post.get_title());
    std::stringstream content_ss(post.get_content());
    std::string tmp;

    while(!title_ss.eof()){
        title_ss>>tmp;
        tot_cnt++;
        if(keyword_set.find(tmp) != keyword_set.end()) same_cnt++;
    }

    while(!content_ss.eof()){
        content_ss>>tmp;
        tot_cnt++;
        if(keyword_set.find(tmp) != keyword_set.end()) same_cnt++;
    }
}

PostSimilar::PostSimilar(Post post, const std::set<std::string> keyword_set):post(post) {
    calculate_cnt(keyword_set);
}
