package game.twiddle;

import game.Game;
import ui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Represents a Twiddle game that involves rotating squares within a graphical interface.
 */
public class Twiddle extends Game {

    /**
     * The file used for saving the game state.
     */
    private final File saveFile;

    /**
     * The total number of cells in a row.
     */
    public static int cells;

    /**
     * Half the size of each square in pixels.
     */
    final static int size = 47;

    /**
     * The offset used for positioning elements.
     */
    final static int offset = 100;

    /**
     * Indicates if an animation is currently in progress.
     */
    static boolean animating = false;

    /**
     * Indicates whether squares have to by oriented in the right direction.
     */
    static boolean orientable;

    /**
     * List of squares in the game.
     */
    List<SquareRotatable> squares = new ArrayList<>();

    /**
     * List of buttons used for interaction.
     */
    List<Rectangle> buttons = new ArrayList<>();

    /**
     * List of previous moves made in the game.
     */
    LinkedList<TwiddleMove> prevMoves = new LinkedList<>();
    /**
     * List of next moves that can be redone in the game.
     */
    LinkedList<TwiddleMove> nextMoves = new LinkedList<>();


    /**
     * Constructor for Twiddle initializing the game properties and interface.
     */
    public Twiddle() {
        saveFile = new File("saves/twiddle.ser");

        loadGame();
        setSizes();
        setLayout(new OverlayLayout(this));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!animating) {
                    //rotate(0,1);
                    boolean ccw = e.getButton() != MouseEvent.BUTTON3;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).contains(e.getPoint())) {
                            TwiddleMove move = new TwiddleMove(i, ccw);
                            rotate(move);
                            prevMoves.addFirst(move);
                            nextMoves = new LinkedList<>();
                            Main.getGameWindow().getBottom().setUndo(true);
                            Main.getGameWindow().getBottom().setRedo(false);
                        }
                    }
                }
            }
        });
        setVisible(true);
    }

    /**
     * Constructor for Twiddle initializing the game with a specified number of cells and orientation.
     *
     * @param n The number of cells in the game.
     * @param o Indicates if squares have to be oriented in the right direction.
     */
    public Twiddle(int n, boolean o) {
        saveFile = new File("saves/twiddle.ser");
        setSizes();
        setLayout(new OverlayLayout(this));

        generateGame(n, o);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!animating) {
                    //rotate(0,1);
                    boolean ccw = e.getButton() != MouseEvent.BUTTON3;
                    for (int i = 0; i < buttons.size(); i++) {
                        if (buttons.get(i).contains(e.getPoint())) {
                            TwiddleMove move = new TwiddleMove(i, ccw);
                            rotate(move);
                            prevMoves.addFirst(move);
                            nextMoves = new LinkedList<>();
                            Main.getGameWindow().getBottom().setUndo(true);
                            Main.getGameWindow().getBottom().setRedo(false);
                        }
                    }
                }
            }
        });
        setVisible(true);
    }

    /**
     * Represents a move made in the Twiddle game.
     */
    public static class TwiddleMove implements Serializable {
        int pos;
        boolean ccw;

        public TwiddleMove(int p, boolean c) {
            pos = p;
            ccw = c;
        }

        public TwiddleMove undoMove() {
            return new TwiddleMove(pos, !ccw);
        }

        public String toString() {
            return pos + "," + ccw;
        }
    }


    /**
     * Reverts the last move the player made.
     */
    @Override
    public void undo() {
        if (!animating && !prevMoves.isEmpty()) {
            TwiddleMove move = prevMoves.removeFirst();
            rotate(move.undoMove());
            nextMoves.addFirst(move);
            if (prevMoves.isEmpty()) {
                Main.getGameWindow().getBottom().setUndo(false);
            }
            Main.getGameWindow().getBottom().setRedo(true);
        }
    }

    /**
     * Redoes the last move the player reverted (if possible).
     */
    @Override
    public void redo() {
        if (!animating && !nextMoves.isEmpty()) {
            TwiddleMove move = nextMoves.removeFirst();
            rotate(move);
            prevMoves.addFirst(move);
            if (nextMoves.isEmpty()) {
                Main.getGameWindow().getBottom().setRedo(false);
            }
            Main.getGameWindow().getBottom().setUndo(true);
        }
    }

    /**
     * Does the animation for the move the player made.
     *
     * @param m the move the player made.
     */
    public void rotate(TwiddleMove m) {

        int row = m.pos / (cells - 1);
        int col = m.pos % (cells - 1);
        int ccw = m.ccw ? 1 : -1;

        System.out.println(row + "," + col + "," + ccw);

        SquareRotatable nw = squares.get(cells * row + col);
        SquareRotatable ne = squares.get(cells * row + col + 1);
        SquareRotatable sw = squares.get(cells * (row + 1) + col);
        SquareRotatable se = squares.get(cells * (row + 1) + col + 1);

        Point center = new Point((int) ((1.75 + m.pos % (cells - 1)) * offset), (int) ((1.5 + m.pos / (cells - 1)) * offset));

        SwingWorker<Integer, Integer> rotater = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() {
                animating = true;
                double theta = Math.PI / 4;
                double r = Math.sqrt(2) * offset / 2;
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    theta -= ccw * Math.PI / 20;
                    {
                        se.setCenterPoint(center.x + r * Math.cos(theta), center.y + r * Math.sin(theta));
                        se.setTheta(se.theta - ccw * Math.PI / 20);
                    }
                    {
                        sw.setCenterPoint(center.x + r * Math.cos(theta + Math.PI / 2), center.y + r * Math.sin(theta + Math.PI / 2));
                        sw.setTheta(sw.theta - ccw * Math.PI / 20);
                    }
                    {
                        nw.setCenterPoint(center.x + r * Math.cos(theta + Math.PI), center.y + r * Math.sin(theta + Math.PI));
                        nw.setTheta(nw.theta - ccw * Math.PI / 20);
                    }
                    {
                        ne.setCenterPoint(center.x + r * Math.cos(theta - Math.PI / 2), center.y + r * Math.sin(theta - Math.PI / 2));
                        ne.setTheta(ne.theta - ccw * Math.PI / 20);
                    }
                    publish(0);
                }
                if (ccw > 0) {
                    squares.remove(nw);
                    squares.remove(se);
                    squares.add(cells * row + col + 1, se);
                    squares.add(cells * (row + 1) + col, nw);
                    nw.setOrientation(nw.getOrientation() - 1);
                    ne.setOrientation(ne.getOrientation() - 1);
                    sw.setOrientation(sw.getOrientation() - 1);
                    se.setOrientation(se.getOrientation() - 1);
                } else {
                    squares.remove(ne);
                    squares.remove(sw);
                    squares.add(cells * row + col, sw);
                    squares.add(cells * (1 + row) + col + 1, ne);
                    nw.setOrientation(nw.getOrientation() + 1);
                    ne.setOrientation(ne.getOrientation() + 1);
                    sw.setOrientation(sw.getOrientation() + 1);
                    se.setOrientation(se.getOrientation() + 1);
                }
                return 0;
            }

            @Override
            protected void process(List<Integer> chunks) {
                repaint();
            }

            @Override
            public void done() {
                for (int i = 0; i < squares.size(); i++) {
                    if (squares.get(i).getTag() != i + 1) {
                        animating = false;
                        return;
                    }
                    if (orientable && squares.get(i).getOrientation() != 0) {
                        animating = false;
                        return;
                    }
                }

                gameEnded();
                animating = false;
            }
        };
        rotater.execute();
    }

    /**
     * Generates a new game without specifying the size of field nor the need for orientation.
     */
    @Override
    public void generateGame() {
        for (SquareRotatable sq : squares) {
            remove(sq);
        }
        setButtons();

        squares = new ArrayList<>();
        for (int i = 0; i < cells * cells; i++) {
            squares.add(new SquareRotatable(i + 1));
        }
        Collections.shuffle(squares);
        int rotsum = 0;
        Random r = new Random();
        for (int i = 0; i < cells * cells; i++) {
            int goal = squares.get(i).getTag() - 1;
            int irow = i / cells;
            int icol = i % cells;
            int grow = goal / cells;
            int gcol = goal % cells;

            int rot;
            if (i + 1 < cells * cells) {
                rot = (2 * r.nextInt(2) + irow - grow + icol - gcol) % 4;
            } else {
                rot = -(rotsum % 4);
            }
            squares.get(i).setOrientation(rot);
            rotsum += rot;

            squares.get(i).setSpot(i);
            add(squares.get(i));
        }
        prevMoves = new LinkedList<>();
        nextMoves = new LinkedList<>();

        Main.getGameWindow().getBottom().setRedo(false);
        Main.getGameWindow().getBottom().setUndo(false);

        saveGame();

        setVisible(false);
        setVisible(true);
    }

    /**
     * Sets the buttons where they should be.
     */
    private void setButtons() {
        buttons = new ArrayList<>();
        for (int i = 0; i < (cells - 1) * (cells - 1); i++) {
            Rectangle rect = new Rectangle((int) ((1.75 + i % (cells - 1)) * offset - 30), (int) ((1.5 + i / (cells - 1)) * offset - 30), 60, 60);
            buttons.add(rect);
        }
    }

    /**
     * Sets the window's size.
     */
    public void setSizes() {
        setMinimumSize(new Dimension((int) ((cells + 1.5) * offset), (cells + 1) * offset));
        if (Main.getGameWindow() != null) {
            Main.getGameWindow().setSize(new Dimension((int) ((cells + 1.5) * offset), (cells + 1) * offset + 150));
            Main.getGameWindow().setVisible(false);
            Main.getGameWindow().setVisible(true);
        }
    }

    /**
     * Generates a game of Twiddle.
     *
     * @param n The size of the playing field (nxn).
     * @param o The need for orientating the squares in the right direction.
     */
    public void generateGame(int n, boolean o) {
        cells = n;
        orientable = o;
        setSizes();
        generateGame();
    }

    public void generateGame(TwiddleMove m) {
        generateGame(m.pos, m.ccw);
    }

    /**
     * Loads the game from the location specified by saveFile.
     */
    @Override
    public void loadGame() {
        for (SquareRotatable sq : squares) {
            remove(sq);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            cells = (Integer) ois.readObject();
            orientable = (Boolean) ois.readObject();
            squares = (ArrayList<SquareRotatable>) ois.readObject();
            prevMoves = (LinkedList<TwiddleMove>) ois.readObject();
            nextMoves = (LinkedList<TwiddleMove>) ois.readObject();

            for (SquareRotatable sq : squares) {
                add(sq);
            }

            setButtons();
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
            ous.writeObject(cells);
            ous.writeObject(orientable);
            ous.writeObject(squares);
            ous.writeObject(prevMoves);
            ous.writeObject(nextMoves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<?> getGameType() {
        return Twiddle.class;
    }

}
