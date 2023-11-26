package game.twiddle;

import game.Game;
import ui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.Math;
import java.util.*;
import java.util.List;


import static java.lang.Thread.sleep;

public class Twiddle extends Game {
    private final File saveFile;
    public static int cells;
    final static int size=47;
    final static int offset=100;
    static boolean animating=false;
    static boolean orientable;
    List<SquareRotatable> squares=new ArrayList<>();
    List<Rectangle> buttons=new ArrayList<>();

    LinkedList<TwiddleMove> prevMoves=new LinkedList<>();
    LinkedList<TwiddleMove> nextMoves=new LinkedList<>();


    public Twiddle(){
        saveFile=new File("saves/twiddle.ser");

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
    public Twiddle(int n, boolean o){
        saveFile=new File("saves/twiddle.ser");
        setSizes();
        setLayout(new OverlayLayout(this));

        generateGame(n,o);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!animating){
                    //rotate(0,1);
                    boolean ccw=e.getButton()!=MouseEvent.BUTTON3;
                    for(int i=0;i<buttons.size();i++){
                        if(buttons.get(i).contains(e.getPoint())){
                            TwiddleMove move=new TwiddleMove(i,ccw);
                            rotate(move);
                            prevMoves.addFirst(move);
                            nextMoves=new LinkedList<>();
                            Main.getGameWindow().getBottom().setUndo(true);
                            Main.getGameWindow().getBottom().setRedo(false);
                        }
                    }
                }
            }
        });
        setVisible(true);
    }

    public static class TwiddleMove implements Serializable{
        int pos;
        boolean ccw;
        public TwiddleMove(int p,boolean c){
            pos=p;
            ccw=c;
        }
        public TwiddleMove undoMove(){
            return new TwiddleMove(pos,!ccw);
        }
        public String toString(){
            return pos+","+ccw;
        }
    }


    @Override
    public void undo(){
        if(!animating&&!prevMoves.isEmpty()){
            TwiddleMove move=prevMoves.removeFirst();
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
            TwiddleMove move=nextMoves.removeFirst();
            rotate(move);
            prevMoves.addFirst(move);
            if(nextMoves.isEmpty()){
                Main.getGameWindow().getBottom().setRedo(false);
            }
            Main.getGameWindow().getBottom().setUndo(true);
        }
    }

    public void rotate(TwiddleMove m) {

        int row=m.pos/(cells-1);
        int col=m.pos%(cells-1);
        int ccw=m.ccw?1:-1;

        System.out.println(row+","+col+","+ccw);

        SquareRotatable nw=squares.get(cells*row+col);
        SquareRotatable ne=squares.get(cells*row+col+1);
        SquareRotatable sw=squares.get(cells*(row+1)+col);
        SquareRotatable se=squares.get(cells*(row+1)+col+1);

        Point center=new Point((int) ((1.75+m.pos%(cells-1))*offset), (int) ((1.5+m.pos/(cells-1))*offset));

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
                //TODO: win condition checking

                animating=false;
            }
        };
        rotater.execute();
    }

    public void finishGame(){

    }

    @Override
    public void generateGame() {
        for(SquareRotatable sq:squares){
            remove(sq);
        }
        setButtons();

        squares=new ArrayList<>();
        for(int i=0;i<cells*cells;i++){
            squares.add(new SquareRotatable(i+1));
        }
        Collections.shuffle(squares);
        int rotsum=0;
        Random r=new Random();
        for(int i=0;i<cells*cells;i++) {
            int goal = squares.get(i).getTag() - 1;
            int irow = i / cells;
            int icol = i % cells;
            int grow = goal / cells;
            int gcol = goal % cells;

            int rot;
            if (i + 1 < cells * cells) {
                rot = (2 * r.nextInt(2) + irow - grow + icol - gcol) % 4;
                squares.get(i).setTheta(rot * Math.PI / 2);
                rotsum += rot;
            } else {
                rot = -(rotsum % 4);
                squares.get(i).setTheta(rot * Math.PI / 2);
                rotsum += rot;
            }

            squares.get(i).setSpot(i);
            add(squares.get(i));
        }
        prevMoves=new LinkedList<>();
        nextMoves=new LinkedList<>();

        Main.getGameWindow().getBottom().setRedo(false);
        Main.getGameWindow().getBottom().setUndo(false);

        saveGame();

        setVisible(false);
        setVisible(true);
    }

    private void setButtons() {
        buttons=new ArrayList<>();
        for(int i=0;i<(cells-1)*(cells-1);i++){
            Rectangle rect=new Rectangle((int) ((1.75+i%(cells-1))*offset-30), (int) ((1.5+i/(cells-1))*offset-30),60,60);
            buttons.add(rect);
        }
    }
    public void setSizes(){
        setMinimumSize(new Dimension((int) ((cells+1.5)*offset),(cells+1)*offset));
        if(Main.getGameWindow()!=null) {
            Main.getGameWindow().setMinimumSize(new Dimension((int) ((cells + 1.5) * offset), (cells + 1) * offset + 150));
            Main.getGameWindow().setSize(new Dimension((int) ((cells + 1.5) * offset), (cells + 1) * offset + 150));
            Main.getGameWindow().setVisible(false);
            Main.getGameWindow().setVisible(true);
        }
    }

    public void generateGame(int n,boolean o){
        cells=n;
        orientable=o;
        setSizes();
        generateGame();
    }
    public void generateGame(TwiddleMove m){
        generateGame(m.pos,m.ccw);
    }
    @Override
    public void loadGame(){
        for(SquareRotatable sq:squares){
            remove(sq);
        }
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(saveFile))){
            cells=(Integer)ois.readObject();
            orientable=(Boolean)ois.readObject();
            squares=(ArrayList<SquareRotatable>) ois.readObject();
            prevMoves=(LinkedList<TwiddleMove>) ois.readObject();
            nextMoves=(LinkedList<TwiddleMove>) ois.readObject();

            for(SquareRotatable sq:squares){
                add(sq);
            }

            setButtons();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        setVisible(false);
        setVisible(true);
    }
    @Override
    public void saveGame(){
        try(ObjectOutputStream ous=new ObjectOutputStream(new FileOutputStream(saveFile))){
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
    public Class<?> getGameType(){
        return Twiddle.class;
    }

}
