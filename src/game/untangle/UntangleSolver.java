package game.untangle;

import game.Game;
import ui.Main;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

import static game.untangle.Untangle.radius;

public class UntangleSolver extends Game {

    private LinkedList<Move> prevMoves=new LinkedList<>();
    private LinkedList<Move> nextMoves=new LinkedList<>();
    MyGraph graph;

    public UntangleSolver(MyGraph graph){
        this.graph=graph;
        setMinimumSize(new Dimension(800,750));
        Main.getGameWindow().setSize(800,750);
        Main.getGameWindow().setResizable(true);
        solve();
        Main.getGameWindow().getBottom().setUndo(false);
        Main.getGameWindow().getBottom().setRedo(true);
        setVisible(true);
    }

    public void solve(){
        Move move1=new Move();
        move1.addColorChange(graph.vertices.get(0),Color.CYAN);
        nextMoves.add(move1);
    }

    @Override
    public void loadGame() {}
    @Override
    public void saveGame() {}
    @Override
    public void generateGame() {
        Main.untangle.generateGame();
        Main.getGameWindow().openGame(Main.untangle);
    }

    @Override
    public void undo() {
        Move prevMove=prevMoves.removeFirst();
        for(Circle c:prevMove.prevColors.keySet()){
            c.setColor(prevMove.prevColors.get(c));
        }
        if(prevMove.move!=null) {
            prevMove.move.circle.setFrame(
                    prevMove.move.posFrom.x - radius,
                    prevMove.move.posFrom.y - radius,
                    2 * radius, 2 * radius
            );
            prevMove.move.circle.x = prevMove.move.posFrom.x;
            prevMove.move.circle.y = prevMove.move.posFrom.y;
        }
        repaint();
        nextMoves.addFirst(prevMove);
        if (prevMoves.isEmpty()) Main.getGameWindow().getBottom().setUndo(false);
        Main.getGameWindow().getBottom().setRedo(true);
    }

    @Override
    public void redo() {
        Move nextMove=nextMoves.removeFirst();
        for(Circle c:nextMove.colorChanges.keySet()){
            if(nextMove.colorChanges.get(c)==null) c.setColor();
            else c.setColor(nextMove.colorChanges.get(c));
        }
        if(nextMove.move!=null){
            nextMove.move.circle.setFrame(
                nextMove.move.posTo.x - radius,
                nextMove.move.posTo.y - radius,
                2 * radius, 2 * radius
            );
            nextMove.move.circle.x = nextMove.move.posTo.x;
            nextMove.move.circle.y = nextMove.move.posTo.y;
        }
        repaint();
        prevMoves.addFirst(nextMove);
        if (nextMoves.isEmpty()) Main.getGameWindow().getBottom().setRedo(false);
        Main.getGameWindow().getBottom().setUndo(true);
    }

    @Override
    public Class<?> getGameType() {
        return UntangleSolver.class;
    }

    @Override
    public void restart() {
        while(!prevMoves.isEmpty()){
            undo();
        }
    }

    class Move{
        HashMap<Circle,Color> colorChanges=new HashMap<>();
        HashMap<Circle,Color> prevColors=new HashMap<>();
        Untangle.UntangleMove move;

        public Move(Untangle.UntangleMove umove){
            move=umove;
        }
        public Move(){}
        public void addColorChange(Circle c){
            colorChanges.put(c,Color.blue);
            if(prevColors.get(c)==null) prevColors.put(c,c.getColor());
        }
        public void addColorChange(Circle c, Color col){
            colorChanges.put(c,col);
            if(prevColors.get(c)==null) prevColors.put(c,c.getColor());
        }
        public void setMove(Untangle.UntangleMove umove){
            move=umove;
        }
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

        gd.setColor(Color.red);
        for (int[] e : graph.edges) {
            Circle v1 = graph.vertices.get(e[0]);
            Circle v2 = graph.vertices.get(e[1]);
            gd.drawLine(v1.getx(), v1.gety(), v2.getx(), v2.gety());
        }

        for (Circle c : graph.vertices) {
            gd.setColor(Color.black);
            gd.setStroke(new BasicStroke(2));
            gd.draw(c);
            gd.setColor(c.getColor());
            gd.fill(c);
        }
    }
}
