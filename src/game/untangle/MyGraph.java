package game.untangle;

import org.jgrapht.Graph;
import org.jgrapht.GraphType;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public class MyGraph implements Serializable{
    List<Circle> vertices=new ArrayList<>();
    Set<int[]> edges=new HashSet<>();
    public MyGraph(List<Circle> v, Set<int[]> e){
        vertices=v;
        edges=e;
    }

    public MyGraph(int n){
        generateGraph(n);
    }

    public void generateGraph(int n){
        Graph<Circle, DefaultEdge> graph=new SimpleGraph<>(DefaultEdge.class);
        vertices=new ArrayList<>(n);
        for(int i=0;i<n;i++){
            Circle v=new Circle((int) (300+230*Math.cos(2*i*Math.PI/n)), (int) (300+230*Math.sin(2*i*Math.PI/n)),Untangle.radius);
            vertices.add(v);
            graph.addVertex(v);
        }
        ArrayList<int[]> pairs=new ArrayList<>();
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                pairs.add(new int[]{i, j});
            }
        }
        Collections.shuffle(pairs);
        Graph<Circle,DefaultEdge> newGraph=graph;
        for(int[] pair:pairs){
            graph.addEdge(vertices.get(pair[0]),vertices.get(pair[1]));
            if(new BoyerMyrvoldPlanarityInspector<>(newGraph).isPlanar()){
                edges.add(pair);
            }else{
                graph.removeEdge(vertices.get(pair[0]),vertices.get(pair[1]));
            }
        }
    }

    public boolean intersects(int[] e1, int[] e2){
        if(e1[0]==e2[0]||e1[0]==e2[1]||e1[1]==e2[0]||e1[1]==e2[1]) return false;
        return Line2D.linesIntersect(
                vertices.get(e1[0]).x,vertices.get(e1[0]).y,
                vertices.get(e1[1]).x,vertices.get(e1[1]).y,
                vertices.get(e2[0]).x,vertices.get(e2[0]).y,
                vertices.get(e2[1]).x,vertices.get(e2[1]).y);
    }
}
