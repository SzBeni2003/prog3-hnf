package game.untangle;

import game.Game;
import ui.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UntangleSolver extends Game {

    private LinkedList<Move> prevMoves=new LinkedList<>();
    private LinkedList<Move> nextMoves=new LinkedList<>();
    static MyGraph graph;

    public UntangleSolver(MyGraph graph){
        this.graph=new MyGraph(graph);
        for(Circle node:graph.vertices){
            node.setColor();
        }
        setMinimumSize(new Dimension(800,750));
        Main.getGameWindow().setSize(800,750);
        Main.getGameWindow().setResizable(false);
        solve();
        Main.getGameWindow().getBottom().setUndo(false);
        Main.getGameWindow().getBottom().setRedo(true);
        setVisible(true);
    }

    public void solve(){
        /*Move move1=new Move();
        move1.addColorChange(graph.vertices.get(0),Color.CYAN);
        move1.setMove(new Move.UntangleMove(0,graph.vertices.get(0).getCenter(),new Point(300,300)));
        nextMoves.add(move1);*/

        //find a triangle
        List<Circle> foundNodes=new ArrayList<>();
        search:
        for(Circle node1: graph.vertices){
            foundNodes.add(node1);
            for(Circle node2: node1.getNeighbors()){
                foundNodes.add(node2);
                List<Circle> commonNeighbours=graph.commonNeighbors(foundNodes);
                if(!commonNeighbours.isEmpty()){
                    Circle node3=commonNeighbours.getFirst();
                    foundNodes.add(node3);
                    Move move=new Move();
                    move.addColorChange(node1,Color.GREEN);
                    move.addColorChange(node2,Color.BLUE);
                    move.addColorChange(node3,Color.RED);
                    nextMoves.add(move);
                    move=new Move();
                    move.setMove(new Move.UntangleMove(node1,node1.getCenter(),new Point(26,26)));
                    nextMoves.add(move);
                    move=new Move();
                    move.setMove(new Move.UntangleMove(node2,node2.getCenter(),new Point(26,624)));
                    nextMoves.add(move);
                    move=new Move();
                    move.setMove(new Move.UntangleMove(node3,node3.getCenter(),new Point(575,325)));
                    nextMoves.add(move);
                    break search;
                } else foundNodes.remove(node2);
            }
            foundNodes.remove(node1);
        }

        solveInTriangle(foundNodes,foundNodes);

        //List<Circle> ab=graph.commonNeighbors(foundNodes.get(0),foundNodes.get(1));ab.removeAll(tetrahedron);
        //List<Circle> ac=graph.commonNeighbors(foundNodes.get(0),foundNodes.get(2));ac.removeAll(tetrahedron);
        //List<Circle> bc=graph.commonNeighbors(foundNodes.get(1),foundNodes.get(2));bc.removeAll(tetrahedron);

    }

    public void solveInTriangle(List<Circle> foundNodes, List<Circle> triangle){
        //tetrahedrons?
        List<Circle> tetrahedron=graph.commonNeighbors(triangle);
        tetrahedron.retainAll(foundNodes);
        Move move = new Move();
        for(Circle c:tetrahedron) {
            move.addColorChange(c,new Color(192, 192, 192));
        }
        nextMoves.add(move);

        switch(tetrahedron.size()){
            case 2 -> {
                //relocation of common nodes
                Move relocate=new Move();
                relocate.setMove(new Move.UntangleMove(tetrahedron.get(0),tetrahedron.get(0).getCenter(),new Point(250,325)));
                nextMoves.add(relocate);
                relocate.setMove(new Move.UntangleMove(tetrahedron.get(1),tetrahedron.get(1).getCenter(),new Point(750,325)));
                nextMoves.add(relocate);
                //első
                
                //második

            }
            case 1 -> {
                //node relocation
                Move relocate=new Move();
                relocate.setMove(new Move.UntangleMove(tetrahedron.getFirst(),tetrahedron.getFirst().getCenter(),new Point(750,325)));
                nextMoves.add(relocate);
                relocate.setMove(new Move.UntangleMove(triangle.get(2),triangle.get(2).getCenter(),new Point(500,325)));
                nextMoves.add(relocate);
                //

            }
            default -> {
                //node relocation
                Move relocate=new Move();
                relocate.setMove(new Move.UntangleMove(triangle.get(2),triangle.get(2).getCenter(),new Point(750,325)));
                nextMoves.add(relocate);
            }
        }

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
            /*prevMove.move.circle.setFrame(
                    prevMove.move.posFrom.x - radius,
                    prevMove.move.posFrom.y - radius,
                    2 * radius, 2 * radius
            );*/
            prevMove.move.circle.setCenter(prevMove.move.posFrom);
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
            /*nextMove.move.circle.setFrame(
                nextMove.move.posTo.x - radius,
                nextMove.move.posTo.y - radius,
                2 * radius, 2 * radius
            );*/
            nextMove.move.circle.setCenter(nextMove.move.posTo);
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
        UntangleMove move;
        static class UntangleMove{
            final Circle circle;
            final Point posFrom;
            final Point posTo;
            public UntangleMove(int n,Point from,Point to){
                circle=graph.vertices.get(n);
                posFrom=from;
                posTo=to;
            }
            public UntangleMove(Circle c,Point from,Point to){
                circle=c;
                posFrom=from;
                posTo=to;
            }
        }

        /*public Move(UntangleMove umove){
            move=umove;
        }*/

        public Move(){}
        public void addColorChange(Circle c){
            colorChanges.put(c,Color.blue);
            prevColors.computeIfAbsent(c, Circle::getColor);
        }
        public void addColorChange(Circle c, Color col){
            colorChanges.put(c,col);
            prevColors.computeIfAbsent(c, Circle::getColor);
        }
        public void setMove(UntangleMove umove){
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
