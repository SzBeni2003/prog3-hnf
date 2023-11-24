package game.twiddle;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static java.lang.Thread.sleep;

public class Twiddle extends Game {
    static int cells;
    final static int size=47;
    final static int offset=100;
    static boolean animating;
    List<SquareRotatable> squares;
    SquareRotatable sq1=new SquareRotatable(300,100,10);
    List<Rectangle> buttons;

    public void rotate(int block, int ccw) {

        int row=block/(cells-1);
        int col=block%(cells-1);

        System.out.println(row+","+col+","+ccw);

        SquareRotatable nw=squares.get(cells*row+col);
        SquareRotatable ne=squares.get(cells*row+col+1);
        SquareRotatable sw=squares.get(cells*(row+1)+col);
        SquareRotatable se=squares.get(cells*(row+1)+col+1);

        Point center=new Point((2+block%(cells-1))*offset,(2+block/(cells-1))*offset);

        SwingWorker<Integer, Integer> rotater = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                animating=true;
                double theta=Math.PI/4;
                double r=Math.sqrt(2)*offset/2;
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(50);
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

    public Twiddle(int n){
        super();
        setMinimumSize(new Dimension((n+2)*2*size,(n+2)*2*size));
        animating=false;
        cells=n;
        setLayout(new OverlayLayout(this));
        squares=new ArrayList<>();
        buttons=new ArrayList<>();

        for(int i=0;i<n*n;i++){
            squares.add(new SquareRotatable(i+1));
        }
        for(int i=0;i<(n-1)*(n-1);i++){
            Rectangle rect=new Rectangle((2+i%(n-1))*offset-30,(2+i/(n-1))*offset-30,60,60);
            buttons.add(rect);
        }
        Collections.shuffle(squares);
        for(int i=0;i<n*n;i++){
            //TODO:orientáció inicializálása (szummának milyennek kell lenni? 180/360?)
            squares.get(i).setSpot(i);
            add(squares.get(i));
        }

        //TODO:láthatatlan gombok létrehozása és listenerek hozzáadása->rotate()


        /*squares.add(new SquareRotatable(100,100,1));
        squares.add(new SquareRotatable(100,200,3));
        squares.add(new SquareRotatable(200,100,2));
        squares.add(new SquareRotatable(200,200,4));
        add(squares.get(0));
        add(squares.get(1));
        add(squares.get(2));
        add(squares.get(3));
        add(sq1);*/

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(!animating){
                    //rotate(0,1);
                    int ccw;
                    switch(e.getButton()){
                        case MouseEvent.BUTTON3 -> {
                            ccw=-1;
                        }
                        default -> {
                            ccw=1;
                        }
                    }
                    for(int i=0;i<buttons.size();i++){
                        if(buttons.get(i).contains(e.getPoint())){
                            rotate(i,ccw);
                        }
                    }
                }
            }
        });
        setVisible(true);
    }
}
