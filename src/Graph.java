import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Graph {
    ArrayList<Circle> vertices=new ArrayList<>();
    ArrayList<int[]> edges=new ArrayList<>();
    public Graph(ArrayList<Circle> v,ArrayList<int[]> e){
        vertices=v;
        edges=e;
    }
}
