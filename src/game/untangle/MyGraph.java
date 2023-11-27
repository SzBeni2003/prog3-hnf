package game.untangle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class MyGraph {
    ArrayList<Circle> vertices;
    ArrayList<int[]> edges;
    public MyGraph(ArrayList<Circle> v,ArrayList<int[]> e){
        vertices=v;
        edges=e;
    }

    public void addEdge(int j1, int j2){
        if(j1==j2)return;
        Circle i1=vertices.get(j1);Circle i2=vertices.get(j2);
        i1.addNeighbor(i2);
        i2.addNeighbor(i1);
    }

    public boolean staysPlanar(int i1, int i2){


        return false;
    }

    //private boolean check
}
