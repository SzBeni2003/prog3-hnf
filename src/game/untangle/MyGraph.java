package game.untangle;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.*;

public class MyGraph implements Serializable {
    List<Circle> vertices = new ArrayList<>();
    Set<int[]> edges = new HashSet<>();

    public MyGraph(List<Circle> v, Set<int[]> e) {
        vertices = v;
        edges = e;
        for(int[] edge:e){
            addEdge(edge);
        }
    }

    public MyGraph(MyGraph graph){
        vertices=new ArrayList<>();
        for(Circle c: graph.vertices){
            Circle circle=new Circle(c);
            vertices.add(circle);
        }
        edges=new HashSet<>(graph.edges);
        for(int[] edge: graph.edges){
            addEdge(edge);
        }
    }

    public void addEdge(Circle e1,Circle e2){
        e1.newNeighbor(e2);
        e2.newNeighbor(e1);
    }

    public void addEdge(int[] pair){
        vertices.get(pair[0]).newNeighbor(vertices.get(pair[1]));
        vertices.get(pair[1]).newNeighbor(vertices.get(pair[0]));
    }

    public List<Circle> commonNeighbors(List<Circle> nodes){
        if(nodes.size()<=0) return new ArrayList<>();
        ArrayList<Circle> neighbours=new ArrayList<>(vertices);
        for(Circle node:nodes){
            neighbours.retainAll(node.getNeighbors());
        }
        return neighbours;
    }
    public List<Circle> commonNeighbors(Circle c1, Circle c2, Circle... others){
        ArrayList<Circle> nodes=new ArrayList<>();
        nodes.add(c1);nodes.add(c2);
        for(Circle other:others){
            nodes.add(other);
        }
        return commonNeighbors(nodes);
    }

    public Set<Circle> neighbors(List<Circle> group){
        HashSet<Circle> neighbours=new HashSet<>();
        for(Circle c:group){
            neighbours.addAll(c.getNeighbors());
        }
        neighbours.removeAll(group);
        return neighbours;
    }

    public boolean connected(List<Circle> group1, List<Circle> group2){
        Set<Circle> a=neighbors(group1);
        a.retainAll(group2);
        return !a.isEmpty();
    }

    public MyGraph(int n) {
        generateGraph(n);
    }

    public void generateGraph(int n) {
        Graph<Circle, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        vertices = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Circle v = new Circle((int) (300 + 230 * Math.cos(2 * i * Math.PI / n)), (int) (300 + 230 * Math.sin(2 * i * Math.PI / n)), Untangle.radius);
            vertices.add(v);
            graph.addVertex(v);
        }
        ArrayList<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pairs.add(new int[]{i, j});
            }
        }
        Collections.shuffle(pairs);
        for (int[] pair : pairs) {
            graph.addEdge(vertices.get(pair[0]), vertices.get(pair[1]));
            if (new BoyerMyrvoldPlanarityInspector<>(graph).isPlanar()) {
                edges.add(pair);
                addEdge(pair);
            } else {
                graph.removeEdge(vertices.get(pair[0]), vertices.get(pair[1]));
            }
        }
    }

    public boolean intersects(int[] e1, int[] e2) {
        if (e1[0] == e2[0] || e1[0] == e2[1] || e1[1] == e2[0] || e1[1] == e2[1]) return false;
        return Line2D.linesIntersect(
                vertices.get(e1[0]).x, vertices.get(e1[0]).y,
                vertices.get(e1[1]).x, vertices.get(e1[1]).y,
                vertices.get(e2[0]).x, vertices.get(e2[0]).y,
                vertices.get(e2[1]).x, vertices.get(e2[1]).y
        );
    }
}
