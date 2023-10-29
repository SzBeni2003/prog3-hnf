import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Untangle extends JPanel {
    static Graph graph;
    Point offset;
    static private final int radius = 10;

    Untangle(){
        super();
        {//mintagráf
            ArrayList<Circle> v=new ArrayList<>();
            v.add(new Circle(100,100, radius));
            v.add(new Circle(200,100,radius));
            v.add(new Circle(100,200,radius));
            v.add(new Circle(200,200,radius));
            int[] e1={0,1};
            int[] e2={0,2};
            int[] e3={1,3};
            int[] e4={1,2};
            ArrayList<int[]> e=new ArrayList<>();
            e.add(e1);e.add(e2);e.add(e3);e.add(e4);
            graph=new Graph(v,e);
        }

    }

    public void paint(Graphics g){
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gd=(Graphics2D) g.create();
        gd.setColor(Color.blue);
        for(Circle c: graph.vertices){
            gd.fill(c);
        }
        gd.setColor(Color.red);
        for(int[] e: graph.edges){
            Circle v1=graph.vertices.get(e[0]);
            Circle v2=graph.vertices.get(e[1]);
            gd.drawLine(v1.getx(),v1.gety(),v2.getx(),v2.gety());
        }
    }
}
