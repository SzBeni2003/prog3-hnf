package game.twiddle;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import static java.lang.Thread.sleep;

public class Twiddle extends Game {
    static int cells;
    final static int size=47;
    final static int offset=100;
    static boolean animating;
    List<SquareRotatable> squares;
    SquareRotatable sq1=new SquareRotatable(300,100,10);

    public void rotate(SquareRotatable sq) {
        //TODO:Implementing this for 2x2 blocks (different attributes)
        SwingWorker<Integer, Integer> rotater = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() throws Exception {
                animating=true;
                double x0=350;
                double y0=150;
                double theta=5*Math.PI/4;
                double r=Math.sqrt(2)*50;
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    theta-=Math.PI / 20;
                    sq.setCenterPoint(x0+r*Math.cos(theta),y0+r*Math.sin(theta));
                    sq.setTheta(sq.theta - Math.PI / 20);
                    publish(0);
                }
                return 0;
            }

            @Override
            protected void process(List<Integer> chunks) {
                repaint();
            }

            @Override
            public void done(){
                //TODO: ArrayList-ben helyükre tenni az elemeket; spotok módosítása
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

        /*for(int i=0;i<n*n;i++){
            squares.add(new SquareRotatable(i+1));
        }
        Collections.shuffle(squares);
        for(int i=0;i<n*n;i++){
            //TODO:orientáció inicializálása (szummának milyennek kell lenni? 180/360?)
            squares.get(i).setSpot(i);
            add(squares.get(i));
        }*/

        //TODO:láthatatlan gombok létrehozása és listenerek hozzáadása->rotate()


        squares.add(new SquareRotatable(100,100,1));
        squares.add(new SquareRotatable(100,200,3));
        squares.add(new SquareRotatable(200,100,2));
        squares.add(new SquareRotatable(200,200,4));
        add(squares.get(0));
        add(squares.get(1));
        add(squares.get(2));
        add(squares.get(3));
        add(sq1);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                rotate(sq1);
            }
        });
        setVisible(true);
    }
}
