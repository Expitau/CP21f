#include "admin_ui.h"
#include "io_util.h"

AdminUI::AdminUI(ShoppingDB &db, std::ostream& os): UI(db, os) { }

void AdminUI::add_product(std::string name, int price) {
    // TODO: For problem 1-1
    if(price <= 0){
        os << "ADMIN_UI: Invalid price." << std::endl;
        return;
    }

    db.add_product(name, price);
    os << "ADMIN_UI: "<< name << " is added to the database." << std::endl;
}

void AdminUI::edit_product(std::string name, int price) {
    // TODO: For problem 1-1
    Product* product = db.get_product(name);
    if(product == nullptr){
        os << "ADMIN_UI: Invalid product name." << std::endl;
        return;
    }
    if(price <= 0){
        os << "ADMIN_UI: Invalid price." << std::endl;
        return;
    }

    bool ret = db.edit_product(name, price);
    if(ret){
        os << "ADMIN_UI: " << name << " is modified from the database." << std::endl;
    }else{
        os << "ADMIN_UI: Error!?" << std::endl;
    }
}

void AdminUI::list_products() {
    // TODO: For problem 1-1
    std::list<Product> products_list;
    for(Product* product : db.get_products()) products_list.push_back(*product);

    os << "ADMIN_UI: Products: ";
    OutputUtil opu(os);
    opu.print_product_list(products_list);
    os << std::endl;
}
