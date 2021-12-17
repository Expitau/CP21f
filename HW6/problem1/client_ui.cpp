#include <vector>
#include "io_util.h"
#include "client_ui.h"
#include "product.h"
#include "user.h"

ClientUI::ClientUI(ShoppingDB &db, std::ostream& os) : UI(db, os), current_user(nullptr) {

}

void ClientUI::signup(std::string username, std::string password, bool premium) {
    // TODO: For problem 1-2
    db.add_user(username, password, premium);
    os << "CLIENT_UI: " << username << " is signed up." << std::endl;
}

void ClientUI::login(std::string username, std::string password) {
    // TODO: For problem 1-2
    if(current_user != nullptr){
        os << "CLIENT_UI: Please logout first." << std::endl;
        return;
    }

    User * user = db.get_user(username);
    if(user == nullptr || !(user->auth(password))){
        os << "CLIENT_UI: Invalid username or password." << std::endl;
        return;
    }

    current_user = user;
    os << "CLIENT_UI: " << username << " is logged in." << std::endl;

}

void ClientUI::logout() {
    // TODO: For problem 1-2
    if(current_user == nullptr){
        os << "CLIENT_UI: There is no logged-in user." << std::endl;
        return;
    }

    std::string username = current_user->name;
    current_user = nullptr;
    os << "CLIENT_UI: " << username << " is logged out." << std::endl;
}

void ClientUI::add_to_cart(std::string product_name) {
    // TODO: For problem 1-2
    if(current_user == nullptr){
        os << "CLIENT_UI: Please login first." << std::endl;
        return;
    }

    Product* product = db.get_product(product_name);
    if(product == nullptr){
        os << "CLIENT_UI: Invalid product name." << std::endl;
        return;
    }

    current_user->add_to_cart(product);
    os << "CLIENT_UI: " << product->name << " is added to the cart." << std::endl;
}

void ClientUI::list_cart_products() {
    // TODO: For problem 1-2.
    if(current_user == nullptr){
        os << "CLIENT_UI: Please login first." << std::endl;
        return;
    }

    std::list<Product> cart_list;
    for(Product* product : current_user->cart){
        cart_list.push_back(current_user->get_modified_product(product));
    }

    os << "CLIENT_UI: Cart: ";
    OutputUtil opu(os);
    opu.print_product_list(cart_list);
    os << std::endl;
}

void ClientUI::buy_all_in_cart() {
    // TODO: For problem 1-2
    if(current_user == nullptr){
        os << "CLIENT_UI: Please login first." << std::endl;
        return;
    }

    int tot_price = current_user->buy_all_in_cart();
    os << "CLIENT_UI: Cart purchase completed. Total price: " << tot_price <<"." << std::endl;
}

void ClientUI::buy(std::string product_name) {
    // TODO: For problem 1-2
    if(current_user == nullptr){
        os << "CLIENT_UI: Please login first." << std::endl;
        return;
    }

    Product* product = db.get_product(product_name);
    if(product == nullptr){
        os << "CLIENT_UI: Invalid product name." << std::endl;
        return;
    }

    int price = current_user->get_product_price(product);
    current_user->add_purchase_history(product);
    os << "CLIENT_UI: Purchase completed. Price: " << price << "." << std::endl;
}

void ClientUI::recommend_products() {
    // TODO: For problem 1-3.
    if(current_user == nullptr){
        os << "CLIENT_UI: Please login first." << std::endl;
        return;
    }

    std::list<Product> products = db.get_recommended_products(current_user);

    os << "CLIENT_UI: Recommended products: ";
    OutputUtil opu(os);
    opu.print_product_list(products);
    os << std::endl;
}
