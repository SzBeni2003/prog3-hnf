package game.untangle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Graph {
    ArrayList<Circle> vertices;
    ArrayList<int[]> edges;
    public Graph(ArrayList<Circle> v,ArrayList<int[]> e){
        vertices=v;
        edges=e;
    }
}
