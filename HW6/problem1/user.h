#ifndef PROBLEM1_USER_H
#define PROBLEM1_USER_H

#include <string>
#include <vector>
#include <deque>
#include <list>
#include "product.h"

class User {
public:
    User(std::string name, std::string password);

    const std::string name;
    std::vector<Product*> cart;
    std::vector<Product*> purchase_history;

    bool auth(std::string password);
    void add_purchase_history(Product* product);
    void add_to_cart(Product* product);
    int buy_all_in_cart();
    virtual int get_product_price(Product* product) = 0;
    virtual Product get_modified_product(Product* product) = 0;

private:
    std::string password;
};

class NormalUser : public User {
public:
    NormalUser(std::string name, std::string password);
    int get_product_price(Product *product);
    Product get_modified_product(Product* product);
};

class PremiumUser : public User {
public:
    PremiumUser(std::string name, std::string password);
    int get_product_price(Product *product);
    Product get_modified_product(Product* product);
};

#endif //PROBLEM1_USER_H
