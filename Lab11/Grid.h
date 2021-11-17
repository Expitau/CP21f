#include <iostream>

#ifndef GRID_H
#define GRID_H

//TODO Prob1.4

class Grid {
    int **grid;
    int row, column;
public:
    //TODO Prob1.1 initialize Grid with zeros
    Grid(int r, int c);

    void initialize_with_zeros();

    int getRow() const;

    int getColumn() const;

    int getAt(int r, int c) const;

    void setAt(int r, int c, int v);

    void printGrid();

    Grid(Grid const &g);

    ~Grid();
};

#endif
