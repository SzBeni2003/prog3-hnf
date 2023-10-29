import java.awt.geom.Ellipse2D;

class Circle extends Ellipse2D.Float {
    float x; //the x coordinate of the center point
    float y; //the y coordinate of the center point
    float radius;

    public Circle(float x, float y, float radius) {
        setFrame(x-radius, y-radius, 2*radius, 2*radius);
        this.x=x;
        this.y=y;
        this.radius=radius;
    }

    public int getx() {
        return (int) x;
    }
    public int gety() {
        return (int) y;
    }
}