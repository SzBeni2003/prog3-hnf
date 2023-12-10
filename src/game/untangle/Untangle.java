package game.untangle;

import game.Game;
import ui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;


/**
 * Represents a game of untangle where nodes need to be managed within the graphical interface.
 */
public class Untangle extends Game {

    /**
     * The file used for saving the game state.
     */
    private final File saveFile;


    long clockTime=0;
    long gameTime=0;
    long startTime = -1;
    boolean playing=true;
    final Timer timer = new Timer(1000, e -> {
        if(Main.getGameWindow().isFocused() &&Main.getGameWindow().getGameField().getGameType()==Untangle.class){
            playing=true;
            if (startTime < 0) startTime = System.currentTimeMillis();
            long now = System.currentTimeMillis();
            clockTime = now - startTime;
            repaint();
        }else if(playing){
            gameTime=clockTime+gameTime;
            startTime=-1;
            playing=false;
        }
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
    final MouseAction ma = new MouseAction();

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
        try {
            loadGame();
        }catch (ClassNotFoundException e) {
            generateGame(8);
        }

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
        final int node;
        final Circle circle;
        final Point posFrom;
        final Point posTo;

        public UntangleMove(int n, Point from, Point to) {
            node = n;
            circle=graph.vertices.get(n);
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
            if (nodeDragged != -1) {
                for (int i = 0; i < graph.vertices.size(); i++) {
                    if (i != nodeDragged && graph.vertices.get(i).contains2(e.getPoint())) return;
                }
                updateLocation(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(nodeDragged != -1){
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
    @Override
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
        gd.drawString(df.format(clockTime+gameTime),40,40);
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
        timer.stop();
        prevMoves = new LinkedList<>();
        nextMoves = new LinkedList<>();
        graph = new MyGraph(nodes);

        //saveGame();
        setSizes();
        gameTime=0;
        clockTime=0;
        startTime=-1;
        repaint();
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
    public void loadGame() throws ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            nodes = (Integer) ois.readObject();
            graph = (MyGraph) ois.readObject();
            prevMoves = (LinkedList<UntangleMove>) ois.readObject();
            nextMoves = (LinkedList<UntangleMove>) ois.readObject();
            gameTime=(Long) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            throw e;
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
            ous.writeObject(clockTime+gameTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restart(){
        while(!prevMoves.isEmpty()){
            undo();
        }
    }

    public void solve(){
        restart();
        UntangleSolver solver=new UntangleSolver(graph);
        while(!nextMoves.isEmpty()) redo();
        Main.getGameWindow().openGame(solver);
    }

    /**
     * Reverts the last move the player made.
     */
    @Override
    public void undo() {
        UntangleMove move = prevMoves.removeFirst();
        move.circle.setFrame(move.posFrom.x - radius, move.posFrom.y - radius, 2 * radius, 2 * radius);
        move.circle.setCenter(move.posFrom);
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
        move.circle.setFrame(move.posTo.x - radius, move.posTo.y - radius, 2 * radius, 2 * radius);
        move.circle.setCenter(move.posTo);
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
