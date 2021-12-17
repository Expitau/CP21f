#include "user.h"

User::User(std::string name, std::string password): name(name), password(password) {

}

bool User::auth(std::string password) {
    return this->password == password;
}

void User::add_purchase_history(Product *product) {
    purchase_history.push_back(product);
}

void User::add_to_cart(Product *product) {
    cart.push_back(product);
}

int User::buy_all_in_cart() {
    int tot_price = 0;
    for(Product* product : cart){
        purchase_history.push_back(product);
        tot_price += get_product_price(product);
    }
    cart.clear();
    return tot_price;
}

PremiumUser::PremiumUser(std::string name, std::string password): User(name, password){}

int PremiumUser::get_product_price(Product *product) {
    return (9*(product->price) + 5)/10;
}

Product PremiumUser::get_modified_product(Product *product) {
    return Product(product->name, get_product_price(product));
}

NormalUser::NormalUser(std::string name, std::string password): User(name, password){}

int NormalUser::get_product_price(Product *product) {
    return product->price;
}

Product NormalUser::get_modified_product(Product *product) {
    return *product;
}


