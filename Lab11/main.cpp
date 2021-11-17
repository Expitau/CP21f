#include <iostream>
//TODO Prob1.4 include header files
#include "Grid.h"
#include "Point.h"

//TODO Prob1.2 print a grid with increasing numbers in the shape of the given Grid g
void printNumberGrid(Grid g){
    int row = g.getRow(), column = g.getColumn();
    for(int i=0; i<row; i++){
        for(int j=0; j<column; j++) g.setAt(i, j ,i*column+j);
    }
    g.printGrid();
}

int main() {
    Point p(1,3);
    Grid g(2,3);

    std::cout << "x : " << p.getX() << ", y : " << p.getY() << std::endl;

    g.printGrid();
    printNumberGrid(g);
    g.printGrid();

    Point p1(1, 0);
    Point p2(0, 1);
    Point p3(3, 3);

    g.mark_point(p1);
    g.mark_point(p2);
    g.mark_point(p3);

    g.printGrid();

    return 0;
}
