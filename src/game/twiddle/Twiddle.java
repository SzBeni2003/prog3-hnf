package game.twiddle;

import game.Game;
import ui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.util.*;
import java.util.List;


import static java.lang.Thread.sleep;

public class Twiddle extends Game {
    static int cells;
    final static int size=47;
    final static int offset=100;
    static boolean animating=false;
    static boolean orientable;
    List<SquareRotatable> squares;
    SquareRotatable sq1=new SquareRotatable(300,100,10);
    List<Rectangle> buttons;

    LinkedList<Move> prevMoves=new LinkedList<>();
    LinkedList<Move> nextMoves=new LinkedList<>();


    public Twiddle(int n, boolean o){
        super();
        setMinimumSize(new Dimension((n+2)*2*size,(n+2)*2*size));
        setLayout(new OverlayLayout(this));
        squares=new ArrayList<>();
        buttons=new ArrayList<>();
        generateGame(n,o);

        for(int i=0;i<(n-1)*(n-1);i++){
            Rectangle rect=new Rectangle((2+i%(n-1))*offset-30,(2+i/(n-1))*offset-30,60,60);
            buttons.add(rect);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!animating){
                    //rotate(0,1);
                    boolean ccw=e.getButton()!=MouseEvent.BUTTON3;
                    for(int i=0;i<buttons.size();i++){
                        if(buttons.get(i).contains(e.getPoint())){
                            Move move=new Move(i,ccw);
                            rotate(move);
                            prevMoves.addFirst(move);
                            Main.getGameWindow().getBottom().setUndo(true);
                        }
                    }
                }
            }
        });
        setVisible(true);
    }

    class Move{
        int pos;
        boolean ccw;
        public Move(int p,boolean c){
            pos=p;
            ccw=c;
        }
        public Move undoMove(){
            return new Move(pos,!ccw);
        }
    }


    @Override
    public void undo(){
        if(!animating&&!prevMoves.isEmpty()){
            Move move=prevMoves.removeFirst();
            rotate(move.undoMove());
            nextMoves.addFirst(move);
            if(prevMoves.isEmpty()){
                Main.getGameWindow().getBottom().setUndo(false);
            }
            Main.getGameWindow().getBottom().setRedo(true);
        }
    }

    @Override
    public void redo() {
        if(!animating&&!nextMoves.isEmpty()){
            Move move=nextMoves.removeFirst();
            rotate(move);
            prevMoves.addFirst(move);
            if(nextMoves.isEmpty()){
                Main.getGameWindow().getBottom().setRedo(false);
            }
            Main.getGameWindow().getBottom().setUndo(true);
        }
    }

    public void rotate(Move m) {

        int row=m.pos/(cells-1);
        int col=m.pos%(cells-1);
        int ccw=m.ccw?1:-1;

        System.out.println(row+","+col+","+ccw);

        SquareRotatable nw=squares.get(cells*row+col);
        SquareRotatable ne=squares.get(cells*row+col+1);
        SquareRotatable sw=squares.get(cells*(row+1)+col);
        SquareRotatable se=squares.get(cells*(row+1)+col+1);

        Point center=new Point((2+m.pos%(cells-1))*offset,(2+m.pos/(cells-1))*offset);

        SwingWorker<Integer, Integer> rotater = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                animating=true;
                double theta=Math.PI/4;
                double r=Math.sqrt(2)*offset/2;
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    theta-=ccw*Math.PI / 20;
                    {
                        se.setCenterPoint(center.x + r * Math.cos(theta), center.y + r * Math.sin(theta));
                        se.setTheta(se.theta - ccw*Math.PI / 20);
                    }
                    {
                        sw.setCenterPoint(center.x+r*Math.cos(theta+Math.PI/2),center.y+r*Math.sin(theta+Math.PI/2));
                        sw.setTheta(sw.theta-ccw*Math.PI/20);
                    }
                    {
                        nw.setCenterPoint(center.x+r*Math.cos(theta+Math.PI),center.y+r*Math.sin(theta+Math.PI));
                        nw.setTheta(nw.theta-ccw*Math.PI/20);
                    }
                    {
                        ne.setCenterPoint(center.x+r*Math.cos(theta-Math.PI/2),center.y+r*Math.sin(theta-Math.PI/2));
                        ne.setTheta(ne.theta-ccw*Math.PI/20);
                    }
                    publish(0);
                }
                if(ccw>0){
                    squares.remove(nw);
                    squares.remove(se);
                    squares.add(cells*row+col+1,se);
                    squares.add(cells*(row+1)+col,nw);
                }else{
                    squares.remove(ne);
                    squares.remove(sw);
                    squares.add(cells*row+col,sw);
                    squares.add(cells*(1+row)+col+1,ne);
                }
                return 0;
            }

            @Override
            protected void process(List<Integer> chunks) {
                repaint();
            }

            @Override
            public void done(){
                //TODO: spotok módosítása??

                //TODO: win condition checking
                animating=false;
            }
        };
        rotater.execute();
    }

    public void generateGame(int n,boolean o){
        cells=n;
        orientable=o;
        for(int i=0;i<n*n;i++){
            squares.add(new SquareRotatable(i+1));
        }
        Collections.shuffle(squares);
        int rotsum=0;
        Random r=new Random();
        for(int i=0;i<n*n;i++){
            //TODO:orientáció inicializálása (szummának milyennek kell lenni - 360°)
            int rot;
            if(i<n*n-1) {
                rot = r.nextInt(4);
            }else{
                rot=-(rotsum%4);
            }
            squares.get(i).setTheta(rot * Math.PI / 2);
            rotsum += rot;
            squares.get(i).setSpot(i);
            add(squares.get(i));
        }
    }
    @Override
    public void loadGame(){

    }
    @Override
    public void saveGame(){

    }
}
