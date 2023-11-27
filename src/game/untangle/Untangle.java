package game.untangle;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;
import ui.Main;


public class Untangle extends Game{
    private final File saveFile;
    static int nodes;
    static MyGraph graph;
    static Point offset;
    static final int radius = 6;
    MouseAction ma=new MouseAction();
    int nodeDragged;
    Point from;

    LinkedList<UntangleMove> prevMoves=new LinkedList<>();
    LinkedList<UntangleMove> nextMoves=new LinkedList<>();

    public Untangle(){
        saveFile=new File("saves/untangle.ser");

        setPreferredSize(new Dimension(600,600));
        setSize(600,600);
        loadGame();
        //generateGame(8);

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public Untangle(int n){
        saveFile=new File("saves/untangle.ser");
        generateGame(n);

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }


    public static class UntangleMove implements Serializable{
        int node;
        Point posFrom;
        Point posTo;
        public UntangleMove(int n,Point from,Point to){
            node=n;
            posFrom=from;
            posTo=to;
        }
    }

    private class MouseAction extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            for(int i=0;i<graph.vertices.size();i++){
                Circle node=graph.vertices.get(i);
                if(node.contains(e.getPoint())){
                    nodeDragged=i;
                    from=new Point(node.x,node.y);
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
            Circle c=graph.vertices.get(nodeDragged);
            Point to=new Point(c.x,c.y);
            prevMoves.addFirst(new UntangleMove(nodeDragged,from,to));
            Main.getGameWindow().getBottom().setUndo(true);
            nodeDragged=-1;
            from=null;
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

        for(Circle c: graph.vertices){
            gd.setColor(Color.black);
            gd.setStroke(new BasicStroke(2));
            gd.draw(c);
            gd.setColor(Color.blue);
            gd.fill(c);
        }
    }

    public void generateGame() {
        prevMoves=new LinkedList<>();
        nextMoves=new LinkedList<>();
        graph=new MyGraph(nodes);

        saveGame();

        setVisible(false);
        setVisible(true);
    }
    public void generateGame(int n){
        nodes=n;
        generateGame();

        saveGame();
    }

    @Override
    public void loadGame() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(saveFile))){
            graph=(MyGraph) ois.readObject();
            prevMoves=(LinkedList<UntangleMove>) ois.readObject();
            nextMoves=(LinkedList<UntangleMove>) ois.readObject();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        setVisible(false);
        setVisible(true);
    }

    @Override
    public void saveGame() {
        try(ObjectOutputStream ous=new ObjectOutputStream(new FileOutputStream(saveFile))){
            ous.writeObject(graph);
            ous.writeObject(prevMoves);
            ous.writeObject(nextMoves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undo() {
        UntangleMove move=prevMoves.removeFirst();
        graph.vertices.get(move.node).setFrame(move.posFrom.x-radius,move.posFrom.y-radius,2*radius,2*radius);
        graph.vertices.get(move.node).x=move.posFrom.x;
        graph.vertices.get(move.node).y=move.posFrom.y;
        repaint();
        nextMoves.addFirst(move);
        if(prevMoves.isEmpty())
            Main.getGameWindow().getBottom().setUndo(false);
        Main.getGameWindow().getBottom().setRedo(true);
    }

    @Override
    public void redo() {
        UntangleMove move=nextMoves.removeFirst();
        graph.vertices.get(move.node).setFrame(move.posTo.x-radius,move.posTo.y-radius,2*radius,2*radius);
        graph.vertices.get(move.node).x=move.posTo.x;
        graph.vertices.get(move.node).y=move.posTo.y;
        repaint();
        prevMoves.add(move);
        if(nextMoves.isEmpty())
            Main.getGameWindow().getBottom().setRedo(false);
        Main.getGameWindow().getBottom().setUndo(true);
    }

    @Override
    public Class<?> getGameType() {
        return Untangle.class;
    }
}
