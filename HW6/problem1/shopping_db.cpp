#include "shopping_db.h"

ShoppingDB::ShoppingDB() {

}

void ShoppingDB::add_product(std::string name, int price) {
    products.push_back(new Product(name, price));
}

bool ShoppingDB::edit_product(std::string name, int price) {
    if(price <= 0) return false;
    Product* product = get_product(name);
    if(product == nullptr) return false;
    product->price = price;
    return true;
}

Product* ShoppingDB::get_product(std::string name) {
    auto it = std::find_if(products.begin(), products.end(), [name](Product * p){return p->name == name;});
    if(it == products.end()) return nullptr;
    return *it;
}

std::vector<Product*> ShoppingDB::get_products(){
    return products;
}

void ShoppingDB::add_user(std::string username, std::string password, bool premium) {
    User * newUser;
    if(premium) newUser = new PremiumUser(username, password);
    else newUser = new NormalUser(username, password);
    users.push_back(newUser);
}

User* ShoppingDB::get_user(std::string username) {
    auto it = std::find_if(users.begin(), users.end(), [username](User * p){return p->name == username;});
    if(it == users.end()) return nullptr;
    return *it;
}

std::list<Product> get_top3_products(std::list<Product> const & org_products){
    auto it = org_products.begin();
    std::list<Product> products;

    for(auto it = org_products.begin(); it != org_products.end() && products.size() < 3; ++it){
        std::string name = (it)->name;
        auto it2 = std::find_if(products.begin(), products.end(), [name](Product p){return p.name == name;});
        if(it2 == products.end()){
            products.push_back(*it);
        }
    }

    return products;
}

std::list<Product> ShoppingDB::get_normal_user_candi_products(User* current_user){
    std::list<Product> products;
    for(auto it = current_user->purchase_history.rbegin();
            it != current_user->purchase_history.rend(); ++it){
        products.push_back(current_user->get_modified_product(*it));
    }
    return products;
}

std::list<Product> ShoppingDB::get_premium_user_candi_products(User* current_user){
    std::set<std::string> product_set;
    for(auto it : current_user->purchase_history) product_set.insert(it->name);

    std::vector<std::pair<int, int>> users_info;
    for(int i=0; i<users.size(); i++){
        if(users[i] == current_user) continue;
        int cnt = 0;
        for(Product* product : users[i]->purchase_history){
            if(product_set.find(product->name) != product_set.end()) cnt++;
        }

        users_info.push_back({-cnt, i});
    }
    std::sort(users_info.begin(), users_info.end());

    std::list<Product> products;
    for(auto user_info : users_info){
        User* user = users[user_info.second];
        auto recent_product = user->purchase_history.rbegin();
        if(recent_product == user->purchase_history.rend()) continue;

        Product* product = *recent_product;
        products.push_back(current_user->get_modified_product(product));
    }

    return products;
}

std::list<Product> ShoppingDB::get_recommended_products(User* current_user){
    std::list<Product> products;

    if (dynamic_cast<PremiumUser*>(current_user)) products = get_premium_user_candi_products(current_user);
    else products = get_normal_user_candi_products(current_user);

    return get_top3_products(products);
}
