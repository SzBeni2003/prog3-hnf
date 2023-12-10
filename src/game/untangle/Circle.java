package game.untangle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;

/**
 * A circle.
 */
class Circle extends Ellipse2D.Float {
    int x; //the x coordinate of the center point
    int y; //the y coordinate of the center point
    final int radius; //the radius of the circle
    Color color;

    HashSet<Circle> neighbors=new HashSet<>();

    public Circle(int x, int y, int radius) {
        setFrame(x - radius, y - radius, 2 * radius, 2 * radius);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color=Color.blue;
    }

    public void newNeighbor(Circle c){
        neighbors.add(c);
    }

    public HashSet<Circle> getNeighbors(){
        return neighbors;
    }
    public boolean isNeighbor(Circle c){
        return neighbors.contains(c);
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public Point getCenter(){
        return new Point(x,y);
    }

    public void setCenter(Point p){
        x=p.x;
        y=p.y;
    }

    public Color getColor(){
        return color;
    }
    public void setColor(Color c){color=c;}
    public void setColor(){color=Color.blue;}

    public boolean contains2(Point p) {
        Point translated = new Point(p.x + Untangle.offset.x, p.y + Untangle.offset.y);
        return Point.distanceSq(x, y, translated.x, translated.y) <= 4 * radius * radius;
    }
}