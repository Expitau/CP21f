#include "io_util.h"

OutputUtil::OutputUtil(std::ostream& os): os(os) {}


void OutputUtil::print_product_list(std::list<Product> const& list) {
    auto start = list.begin();
    auto end = list.end();
    auto last = (--list.end());

    os << "[";
    while(start != end){
        os << *(start);
        if(start != last) os << ", ";
        start++;
    }
    os << "]";
}
