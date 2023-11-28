package game.untangle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.SortedSet;

/**
 * A circle.
 */
class Circle extends Ellipse2D.Float {
    int x; //the x coordinate of the center point
    int y; //the y coordinate of the center point
    int radius; //the radius of the circle

    public Circle(int x, int y, int radius) {
        setFrame(x-radius, y-radius, 2*radius, 2*radius);
        this.x=x;
        this.y=y;
        this.radius=radius;
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