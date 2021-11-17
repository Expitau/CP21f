#include "Grid.h"

//TODO Prob1.4

int Grid::markCnt = 1;

Grid::Grid(int r, int c): row(r), column(c) {
    grid = new int*[row];
    for(int i=0; i<row; i++) grid[i] = new int[column];
    initialize_with_zeros();
}

void Grid::initialize_with_zeros() {
    for(int i=0; i<row; i++){
        for(int j=0; j<column; j++) grid[i][j] = 0;
    }
}

int Grid::getRow() const { return row; }

int Grid::getColumn() const { return column; }

int Grid::getAt(int r, int c) const { return grid[r][c]; }

void Grid::setAt(int r, int c, int v) { grid[r][c] = v; }

void Grid::printGrid() {
    for(int i=0; i<row; i++){
        for(int j=0; j<column; j++) std::cout << grid[i][j] << " ";
        std::cout << std::endl;
    }
}

bool Grid::check_valid_point(Point p) {
    return 0 <= p.getX() && p.getX() < row && 0 <= p.getY() && p.getY() < column;
}

void Grid::mark_point(Point p) {
    if(check_valid_point(p)){
        grid[p.getX()][p.getY()] = markCnt++;
    }
}

Grid::Grid(Grid const &g): row(g.getRow()), column(g.getColumn()){
    grid = new int*[row];
    for(int i=0; i<row; i++) grid[i] = new int[column];

    for(int i=0; i<row; i++){
        for(int j=0; j<column; j++) grid[i][j] = g.getAt(i, j);
    }
}

Grid::~Grid(){
    std::cout<<"Clean-up Grid" << std::endl;
    for(int i=0; i<row; i++) delete [] grid[i];
    delete [] grid;
}