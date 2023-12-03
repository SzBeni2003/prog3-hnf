package game.untangle;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;


import ui.Main;


/**
 * Represents a game of untangle where nodes need to be managed within the graphical interface.
 */
public class Untangle extends Game {

    /**
     * The file used for saving the game state.
     */
    private final File saveFile;


    long clockTime=0;
    long startTime = -1;
    Timer timer = new Timer(1000, e -> {
        if (startTime < 0) startTime = System.currentTimeMillis();
        long now = System.currentTimeMillis();
        clockTime = now - startTime;
        repaint();
    });


    /**
     * The total number of nodes in the graph.
     */
    static int nodes;

    /**
     * The graph representing connections between nodes.
     */
    static MyGraph graph;

    /**
     * The offset for node positions.
     */
    static Point offset;

    /**
     * The radius of the nodes.
     */
    static final int radius = 6;

    /**
     * Mouse action handling for the game.
     */
    MouseAction ma = new MouseAction();

    /**
     * The index of the node being dragged.
     */
    int nodeDragged;

    /**
     * The starting point of a node's movement.
     */
    Point from;

    /**
     * The list of previous moves made in the game.
     */
    LinkedList<UntangleMove> prevMoves = new LinkedList<>();

    /**
     * The list of next moves that can be redone in the game.
     */
    LinkedList<UntangleMove> nextMoves = new LinkedList<>();


    /**
     * Constructor for Untangle initializing the game's properties.
     */
    public Untangle() {
        saveFile = new File("saves/untangle.ser");

        setPreferredSize(new Dimension(600, 600));
        setSize(800, 750);
        loadGame();
        //generateGame(8);

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    /**
     * Constructor for Untangle initializing the game with a specified number of nodes.
     *
     * @param n The number of nodes to generate for the game.
     */
    public Untangle(int n) {
        saveFile = new File("saves/untangle.ser");
        generateGame(n);

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }


    /**
     * Represents a move made in the Untangle game.
     */
    public static class UntangleMove implements Serializable {
        int node;
        Point posFrom;
        Point posTo;

        public UntangleMove(int n, Point from, Point to) {
            node = n;
            posFrom = from;
            posTo = to;
        }
    }


    /**
     * Handles mouse actions within the game interface.
     */
    private class MouseAction extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            for (int i = 0; i < graph.vertices.size(); i++) {
                Circle node = graph.vertices.get(i);
                if (node.contains(e.getPoint())) {
                    nodeDragged = i;
                    from = new Point(node.x, node.y);
                    offset = new Point(node.x - e.getX(), node.y - e.getY());
                    if(!timer.isRunning()){
                        if(prevMoves.isEmpty()&&nextMoves.isEmpty()) startTime=-1;
                        timer.start();
                    }
                    return;
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //if(graph.vertices.get(nodeDragged).contains(e.getPoint())){
            if (nodeDragged != -1) {
                //biztos hogy körökkel szeretnénk játszani, és nem négyzetekkel??
                for (int i = 0; i < graph.vertices.size(); i++) {
                    if (i != nodeDragged && graph.vertices.get(i).contains2(e.getPoint())) return;
                }
                updateLocation(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            offset = null;
            Circle c = graph.vertices.get(nodeDragged);
            Point to = new Point(c.x, c.y);
            prevMoves.addFirst(new UntangleMove(nodeDragged, from, to));
            Main.getGameWindow().getBottom().setUndo(true);
            nodeDragged = -1;
            from = null;

            for (int[] e1 : graph.edges) {
                for (int[] e2 : graph.edges) {
                    if (graph.intersects(e1, e2)) return;
                }
            }
            timer.stop();
            gameEnded();
        }
    }

    /**
     * Updates the location of a node based on mouse movement.
     *
     * @param e The MouseEvent triggering the node update.
     */
    public void updateLocation(MouseEvent e) {
        Point p = new Point(offset.x + e.getX(), offset.y + e.getY());
        Circle c = graph.vertices.get(nodeDragged);
        c.setFrame(p.x - radius, p.y - radius, 2 * radius, 2 * radius);
        c.x = p.x;
        c.y = p.y;
        repaint();
    }

    /**
     * Paints the game interface with nodes and edges.
     *
     * @param g The Graphics object used for painting.
     */
    public void paint(Graphics g) {
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Paints the components of the game interface.
     *
     * @param g The Graphics object used for painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gd = (Graphics2D) g.create();
        gd.setColor(Color.BLACK);
        gd.setFont(g.getFont().deriveFont(30f));
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        gd.drawString(df.format(clockTime),40,40);
        gd.setColor(Color.red);
        //gd.setStroke(new BasicStroke(3f));
        for (int[] e : graph.edges) {
            Circle v1 = graph.vertices.get(e[0]);
            Circle v2 = graph.vertices.get(e[1]);
            gd.drawLine(v1.getx(), v1.gety(), v2.getx(), v2.gety());
        }

        for (Circle c : graph.vertices) {
            gd.setColor(Color.black);
            gd.setStroke(new BasicStroke(2));
            gd.draw(c);
            gd.setColor(Color.blue);
            gd.fill(c);
        }
    }


    /**
     * Generates a new game without specifying the number of nodes.
     */
    public void generateGame() {
        prevMoves = new LinkedList<>();
        nextMoves = new LinkedList<>();
        graph = new MyGraph(nodes);

        saveGame();

        setVisible(false);
        setVisible(true);
    }

    /**
     * Generates a new game with a specified number of nodes.
     *
     * @param n The number of nodes to generate for the game.
     */
    public void generateGame(int n) {
        nodes = n;
        generateGame();

        saveGame();
    }


    /**
     * Loads the game from the location specified by saveFile.
     */
    @Override
    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            nodes = (Integer) ois.readObject();
            graph = (MyGraph) ois.readObject();
            prevMoves = (LinkedList<UntangleMove>) ois.readObject();
            nextMoves = (LinkedList<UntangleMove>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        setVisible(false);
        setVisible(true);
    }


    /**
     * Saves the game to the location specified by saveFile.
     */
    @Override
    public void saveGame() {
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            ous.writeObject(nodes);
            ous.writeObject(graph);
            ous.writeObject(prevMoves);
            ous.writeObject(nextMoves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reverts the last move the player made.
     */
    @Override
    public void undo() {
        UntangleMove move = prevMoves.removeFirst();
        graph.vertices.get(move.node).setFrame(move.posFrom.x - radius, move.posFrom.y - radius, 2 * radius, 2 * radius);
        graph.vertices.get(move.node).x = move.posFrom.x;
        graph.vertices.get(move.node).y = move.posFrom.y;
        repaint();
        nextMoves.addFirst(move);
        if (prevMoves.isEmpty()) Main.getGameWindow().getBottom().setUndo(false);
        Main.getGameWindow().getBottom().setRedo(true);
    }

    /**
     * Redoes the last move the player reverted (if possible).
     */
    @Override
    public void redo() {
        UntangleMove move = nextMoves.removeFirst();
        graph.vertices.get(move.node).setFrame(move.posTo.x - radius, move.posTo.y - radius, 2 * radius, 2 * radius);
        graph.vertices.get(move.node).x = move.posTo.x;
        graph.vertices.get(move.node).y = move.posTo.y;
        repaint();
        prevMoves.add(move);
        if (nextMoves.isEmpty()) Main.getGameWindow().getBottom().setRedo(false);
        Main.getGameWindow().getBottom().setUndo(true);
    }

    public void setSizes() {
        setMinimumSize(new Dimension(600,750));
        if (Main.getGameWindow() != null) {
            Main.getGameWindow().setSize(600,750);
            Main.getGameWindow().setVisible(false);
            Main.getGameWindow().setVisible(true);
            Main.getGameWindow().setResizable(true);
            Main.getGameWindow().getBottom().setUndo(false);
            Main.getGameWindow().getBottom().setRedo(false);
            if(!prevMoves.isEmpty())Main.getGameWindow().getBottom().setUndo(true);
            if(!nextMoves.isEmpty())Main.getGameWindow().getBottom().setRedo(true);
        }
    }

    @Override
    public Class<?> getGameType() {
        return Untangle.class;
    }
}
