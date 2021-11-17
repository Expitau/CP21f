#include <iostream>

#ifndef POINT_H
#define POINT_H

//TODO Prob1.4
class Point {
    int x, y;
public:
    Point(int X, int Y);
    int getX() const;
    int getY() const;
};

#endif
