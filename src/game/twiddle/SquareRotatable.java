package game.twiddle;

import javax.swing.*;
import java.awt.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

public class SquareRotatable extends JComponent {
    double x;
    double y;
    double r;
    double theta;
    int tag;

    public SquareRotatable(int tag){this(100,100,tag);}
    public SquareRotatable(double x, double y, int tag){
        this.x=x;
        this.y=y;
        this.r=Twiddle.size;
        this.tag=tag;
    }

    public void setSpot(int spot) {
        setCenterPoint((1.5+spot%Twiddle.cells)*Twiddle.offset,(1.5+spot/Twiddle.cells)*Twiddle.offset);
    }
    public void setCenterPoint(double x,double y){
        this.x=x;
        this.y=y;
    }
    public void setCenterPoint(int x,int y){
        this.x= x;
        this.y= y;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D gd=(Graphics2D) g.create();

        double q=sqrt(2)*r;
        int[] sxpoints={
                (int) (x+q*cos(theta+PI/4)),
                (int) (x+q*cos(theta+3*PI/4)),
                (int) (x+q*cos(theta+5*PI/4)),
                (int) (x+q*cos(theta+7*PI/4))
        };
        int[] sypoints={
                (int) (y+q*sin(theta+1*PI/4)),
                (int) (y+q*sin(theta+3*PI/4)),
                (int) (y+q*sin(theta+5*PI/4)),
                (int) (y+q*sin(theta+7*PI/4))
        };
        gd.setColor(Color.GRAY);
        gd.fill(new Polygon(sxpoints,sypoints,4));

        if(Twiddle.orientable) {
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

        String t=String.valueOf(tag);
        gd.setColor(Color.BLACK);
        gd.setFont(g.getFont().deriveFont(20f));
        if(tag<=9)
            gd.drawString(t, (int) x-5, (int) y+10);
        else gd.drawString(t,(int) x-10,(int) y+10);
    }

    /*@Override
    public boolean contains(double x, double y) {
        if(theta%(Math.PI/2)==0){
            return (x<=this.x+r)&&(x>=this.x-r)&&(y<=this.y+r)&&(y>=this.y-r);
        }else{
            double b1= Math.abs((Math.sin(theta)/r)*(y-this.y+(x-this.x)/Math.tan(theta)));
            double b2= Math.abs((Math.sin(theta-Math.PI/2)/r)*(y-this.y+(x-this.x)/Math.tan(theta-Math.PI/2)));
            return (b1<=1)&&(b2<=1);
        }
    }*/

    /*@Override
    public boolean contains(Point2D p) {
        return contains(p.getX(),p.getY());
    }*/

}
