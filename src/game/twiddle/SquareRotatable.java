package game.twiddle;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

/**
 * Represents a rotatable square component within the Twiddle game.
 */
public class SquareRotatable extends JComponent {

    /**
     * The x-coordinate of the square's center point.
     */
    double x;
    /**
     * The y-coordinate of the square's center point.
     */
    double y;
    /**
     * The radius of the square.
     */
    double r;
    /**
     * The rotation angle of the square.
     */
    double theta;
    /**
     * The tag identifying the square.
     */
    int tag;
    int orientation;

    /**
     * Constructs a SquareRotatable with a given tag, setting default x and y coordinates.
     *
     * @param tag The tag identifying the square.
     */
    public SquareRotatable(int tag) {
        this(100, 100, tag);
    }

    /**
     * Constructs a SquareRotatable with specified x, y coordinates, and a tag.
     *
     * @param x   The x-coordinate of the square.
     * @param y   The y-coordinate of the square.
     * @param tag The tag identifying the square.
     */
    public SquareRotatable(double x, double y, int tag) {
        this.x = x;
        this.y = y;
        this.r = Twiddle.size;
        this.tag = tag;
    }

    /**
     * Sets the position of the square based on a specific spot.
     *
     * @param spot The spot position for the square.
     */
    public void setSpot(int spot) {
        setCenterPoint((1.25 + spot % Twiddle.cells) * Twiddle.offset, (1 + spot / Twiddle.cells) * Twiddle.offset);
    }

    /**
     * Sets the center point of the square to the given x and y coordinates.
     *
     * @param x The x-coordinate for the center point.
     * @param y The y-coordinate for the center point.
     */
    public void setCenterPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Sets the rotation angle of the square.
     *
     * @param theta The rotation angle in radians.
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void setOrientation(int o) {
        orientation = o;
        setTheta(o * PI / 2);
    }

    public int getOrientation() {
        return orientation % 4;
    }

    /**
     * Paints the square with its graphical representation.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gd = (Graphics2D) g.create();

        double q = sqrt(2) * r;
        int[] sxpoints = {
                (int) (x + q * cos(theta + PI / 4)),
                (int) (x + q * cos(theta + 3 * PI / 4)),
                (int) (x + q * cos(theta + 5 * PI / 4)),
                (int) (x + q * cos(theta + 7 * PI / 4))
        };
        int[] sypoints = {
                (int) (y + q * sin(theta + 1 * PI / 4)),
                (int) (y + q * sin(theta + 3 * PI / 4)),
                (int) (y + q * sin(theta + 5 * PI / 4)),
                (int) (y + q * sin(theta + 7 * PI / 4))
        };
        gd.setColor(Color.GRAY);
        gd.fill(new Polygon(sxpoints, sypoints, 4));

        if (Twiddle.orientable) {
            int[] txpoints = {
                    (int) (x - 0.8 * r * cos(theta - PI / 6)),
                    (int) (x - 0.8 * r * cos(theta + 0.5 * PI)),
                    (int) (x - 0.8 * r * cos(theta + 7 * PI / 6))
            };
            double s = 0.15 * r * sin(PI / 2 + theta);
            int[] typoints = {
                    (int) (y + s - 0.8 * r * sin(theta - PI / 6)),
                    (int) (y + s - 0.8 * r * sin(theta + 0.5 * PI)),
                    (int) (y + s - 0.8 * r * sin(theta + 7 * PI / 6))
            };
            gd.setColor(Color.lightGray);
            gd.fill(new Polygon(txpoints, typoints, 3));
        }

        String t = String.valueOf(tag);
        gd.setColor(Color.BLACK);
        gd.setFont(g.getFont().deriveFont(20f));
        if (tag <= 9)
            gd.drawString(t, (int) x - 5, (int) y + 10);
        else gd.drawString(t, (int) x - 10, (int) y + 10);
    }

    /**
     * Retrieves the tag identifying the square.
     *
     * @return The tag identifying the square.
     */
    public int getTag() {
        return tag;
    }
}
