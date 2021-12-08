#include <algorithm>
#include "restaurant_app.h"

#include <iostream>
#include <iomanip>


void RestaurantApp::rate(string target, int rate) {
    shared_ptr<vector<int>> ratings = find_restaurant(target);
    if(!ratings){
        vector<int> ratings;
        ratings.push_back(rate);
        restaurants[target] = std::make_shared<vector<int>>(ratings);
        return;
    }
    ratings->push_back(rate);
    std::sort(ratings->begin(), ratings->end());
}


void RestaurantApp::list() {
    for(auto it : restaurants) std::cout << it.first << " ";
    std::cout << std::endl;
}


void RestaurantApp::show(string target) {
    shared_ptr<vector<int>> ratings = find_restaurant(target);
    if(!ratings){
        std::cout << target << " does not exist." << std::endl;
        return;
    }

    for(auto it : *ratings) std::cout << it << " ";
    std::cout << std::endl;
}


void RestaurantApp::ave(string target){
    shared_ptr<vector<int>> ratings = find_restaurant(target);
    if(!ratings){
        std::cout << target << " does not exist." << std::endl;
        return;
    }

    double sum = 0;
    for(auto it : *ratings) sum += it;
    std::cout.setf(std::ios::fixed);
    std::cout.precision(2);
    std::cout << ((double)sum)/ratings->size() << std::endl;
}


void RestaurantApp::del(string target, int rate) {
    shared_ptr<vector<int>> ratings = find_restaurant(target);
    if(!ratings){
        std::cout << target << " does not exist." << std::endl;
        return;
    }

    ratings->erase(std::remove(ratings->begin(), ratings->end(), rate), ratings->end());
    if(ratings->size() == 0) restaurants.erase(target);
}


void RestaurantApp::cheat(string target, int rate) {
    shared_ptr<vector<int>> ratings = find_restaurant(target);
    if(!ratings){
        std::cout << target << " does not exist." << std::endl;
        return;
    }

    ratings->erase(ratings->begin(), std::lower_bound(ratings->begin(), ratings->end(), rate));
    if(ratings->size() == 0) restaurants.erase(target);
}


shared_ptr<vector<int>> RestaurantApp::find_restaurant(string target) {
    auto it = restaurants.find(target);
    if (it == restaurants.end()) {
        return nullptr;
    }
    return restaurants[target];
}
