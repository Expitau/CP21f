#include <iostream>
//TODO Prob1.4 include header files


class Point {
    int x, y;
public:
    //TODO Prob1.1 initialize Point and print x and y
     Point(int, int){}

     int getX() const {return -1;}

     int getY() const {return -1;}

};

class Grid {
    int **grid;
    int row, column;
public:
    //TODO Prob1.1 initialize Grid with zeros
    Grid(int, int) {}

    void initialize_with_zeros() {}

    int getRow() const { return -1; }

    int getColumn() const { return -1; }

    int getAt(int r, int c) const { return -1; }

    void setAt(int r, int c, int v) {}

    void printGrid() {}

    //TODO Prob1.2 create explicit copy constructor
    Grid(Grid const &g) {}

    //TODO Prob1.3 Add proper clean-up code!
     ~Grid(){}
};


//TODO Prob1.2 print a grid with increasing numbers in the shape of the given Grid g
void printNumberGrid(Grid g){}

int main() {
    Point p(1,3);
    Grid g(2,3);

    std::cout << "x : " << p.getX() << ", y : " << p.getY() << std::endl;

    g.printGrid();

    return 0;
}
