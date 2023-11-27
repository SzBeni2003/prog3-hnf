package game.untangle;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;


public class Untangle extends Game{
    static int nodes;
    static MyGraph graph;
    static Point offset;
    static private final int radius = 10;
    MouseAction ma=new MouseAction();
    int nodeDragged;

    public Untangle(){
        super();
        generateGame();

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }


    private class MouseAction extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            for(int i=0;i<graph.vertices.size();i++){
                Circle node=graph.vertices.get(i);
                if(node.contains(e.getPoint())){
                    nodeDragged=i;
                    offset=new Point(node.x-e.getX(),node.y-e.getY());
                    return;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //if(graph.vertices.get(nodeDragged).contains(e.getPoint())){
            if(nodeDragged!=-1){
                //biztos hogy körökkel szeretnénk játszani, és nem négyzetekkel??
                for(int i=0;i<graph.vertices.size();i++){
                    if(i!=nodeDragged&&graph.vertices.get(i).contains2(e.getPoint()))
                        return;
                }
                updateLocation(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            offset=null;
            nodeDragged=-1;
            //TODO: win condition checking
        }
    }

    public void updateLocation(MouseEvent e){
        Point p=new Point(offset.x+e.getX(), offset.y+e.getY());
        Circle c=graph.vertices.get(nodeDragged);
        c.setFrame(p.x-radius,p.y-radius,2*radius,2*radius);
        c.x=p.x;
        c.y=p.y;
        repaint();
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
        gd.setColor(Color.red);
        //gd.setStroke(new BasicStroke(3f));
        for(int[] e: graph.edges){
            Circle v1=graph.vertices.get(e[0]);
            Circle v2=graph.vertices.get(e[1]);
            gd.drawLine(v1.getx(),v1.gety(),v2.getx(),v2.gety());
        }
        /*
        for(Circle v1: graph.vertices){
            for(Circle v2:v1.getNeighbours()){
                gd.drawLine(v1.getx(),v1.gety(),v2.getx(),v2.gety());
            }
        }
         */

        for(Circle c: graph.vertices){
            gd.setColor(Color.black);
            gd.setStroke(new BasicStroke(3));
            gd.draw(c);
            gd.setColor(Color.blue);
            gd.fill(c);
        }
    }

    public void generateGame() {
        //mintagráf
        //TODO: pályagenerálás
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
        graph=new MyGraph(v,e);
    }
    public void generateGame(int n){
        nodes=n;
        generateGame();
    }

    @Override
    public void loadGame() {

    }

    @Override
    public void saveGame() {

    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public Class<?> getGameType() {
        return Untangle.class;
    }
}
