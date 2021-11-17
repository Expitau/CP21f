#include <iostream>
#include "Point.h"
#ifndef GRID_H
#define GRID_H

//TODO Prob1.4

class Grid {
    int **grid;
    int row, column;
    static int markCnt;
public:
    //TODO Prob1.1 initialize Grid with zeros
    Grid(int r, int c);

    Grid(int r, int c, int ** g);

    Grid(Grid const &g);

    void initialize_with_zeros();

    int getRow() const;

    int getColumn() const;

    int getAt(int r, int c) const;

    void setAt(int r, int c, int v);

    void printGrid();

    bool check_valid_point(Point p);

    void mark_point(Point p);

    ~Grid();
};

#endif
