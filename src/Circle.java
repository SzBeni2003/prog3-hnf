import java.awt.geom.Ellipse2D;

class Circle extends Ellipse2D.Float {
    int x; //the x coordinate of the center point
    int y; //the y coordinate of the center point
    int radius;

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
}