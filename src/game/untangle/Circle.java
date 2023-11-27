package game.untangle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.SortedSet;

class Circle extends Ellipse2D.Float {
    int x; //the x coordinate of the center point
    int y; //the y coordinate of the center point
    int radius;
    HashSet<Circle> neighbours=new HashSet<>();

    public Circle(int x, int y, int radius) {
        setFrame(x-radius, y-radius, 2*radius, 2*radius);
        this.x=x;
        this.y=y;
        this.radius=radius;
    }

    public void setNeighbours(HashSet<Circle> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbor(Circle n){
        neighbours.add(n);
    }
    public HashSet<Circle> getNeighbours() {
        return neighbours;
    }

    public int getx() {
        return x;
    }
    public int gety() {
        return y;
    }
    public boolean contains2(Point p){
        Point translated=new Point(p.x+Untangle.offset.x,p.y+Untangle.offset.y);
        return Point.distanceSq(x,y,translated.x,translated.y)<=4*radius*radius;
    }
}