#include <iostream>
//TODO Prob1.4 include header files


class Point {
    int x, y;
public:
    //TODO Prob1.1 initialize Point and print x and y
     Point(int X, int Y):x(X), y(Y){
    }

     int getX() const {return x;}

     int getY() const {return y;}

};

class Grid {
    int **grid;
    int row, column;
public:
    //TODO Prob1.1 initialize Grid with zeros
    Grid(int r, int c): row(r), column(c) {
        grid = new int*[row];
        for(int i=0; i<row; i++) grid[i] = new int[column];
        initialize_with_zeros();
    }

    void initialize_with_zeros() {
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++) grid[i][j] = 0;
        }
    }

    int getRow() const { return row; }

    int getColumn() const { return column; }

    int getAt(int r, int c) const { return grid[r][c]; }

    void setAt(int r, int c, int v) { grid[r][c] = v; }

    void printGrid() {
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++) std::cout << grid[i][j] << " ";
            std::cout << std::endl;
        }
    }

    //TODO Prob1.2 create explicit copy constructor
    Grid(Grid const &g): row(g.getRow()), column(g.getColumn()){
        grid = new int*[row];
        for(int i=0; i<row; i++) grid[i] = new int[column];

        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++) grid[i][j] = g.getAt(i, j);
        }
    }

    //TODO Prob1.3 Add proper clean-up code!
     ~Grid(){
        std::cout<<"Clean-up Grid" << std::endl;
        for(int i=0; i<row; i++) delete [] grid[i];
        delete [] grid;
    }
};


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

    return 0;
}
