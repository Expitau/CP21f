#ifndef PROBLEM1_SHOPPING_DB_H
#define PROBLEM1_SHOPPING_DB_H

#include <string>
#include <vector>
#include <algorithm>
#include <set>
#include "user.h"
#include "product.h"

class ShoppingDB {
public:
    ShoppingDB();
    void add_product(std::string name, int price);
    bool edit_product(std::string name, int price);
    Product* get_product(std::string name);
    std::vector<Product*> get_products();

    void add_user(std::string username, std::string password, bool premium);
    User* get_user(std::string username);

    std::list<Product> get_recommended_products(User* current_user);
    std::list<Product> get_premium_user_candi_products(User* current_user);
    std::list<Product> get_normal_user_candi_products(User* current_user);

private:
    std::vector<User*> users;
    std::vector<Product*> products;
};

#endif //PROBLEM1_SHOPPING_DB_H
