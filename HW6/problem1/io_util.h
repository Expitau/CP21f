#ifndef PROBLEM1_IO_UTIL_H
#define PROBLEM1_IO_UTIL_H
#include <iostream>
#include <vector>
#include <deque>
#include <iterator>
#include <algorithm>
#include <list>
#include "product.h"


class OutputUtil{
public:
    std::ostream& os;
    OutputUtil(std::ostream& os);

    void print_product_list(std::list<Product> const& list);
};



#endif //PROBLEM1_IO_UTIL_H
