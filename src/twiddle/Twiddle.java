package twiddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Twiddle extends JPanel implements Runnable{
    static int size;
    JButton b1=new JButton();
    LinkedList<SquareRotatable> squares;
    SquareRotatable sq1=new SquareRotatable(100,100,50,1);

    public void rotate(SquareRotatable sq) {
        SwingWorker<Integer, Integer> rotater = new SwingWorker<Integer, Integer>() {
            @Override
            protected Integer doInBackground() throws Exception {
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sq.setTheta(sq.theta + Math.PI / 20);
                    publish(0);
                }
                return 0;
            }

            @Override
            protected void process(List<Integer> chunks) {
                repaint();
            }
        };
        rotater.execute();
    }

    public Twiddle(int n){
        super();
        size=n;
        setLayout(new GridLayout(n,n));
        squares=new LinkedList<>();
        //for(int i=0;)
        add(sq1);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                rotate(sq1);
            }
        });
        setVisible(true);
    }


    /*public void game(){
        repaint();
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        run();
    }*/
    @Override
    public void run() {
        for(int i=0;i<10;i++) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sq1.setTheta(sq1.theta + Math.PI / 20);
            repaint();
        }
    }



    /*@Override
    public void paint(Graphics g){
        Image dbImage = createImage(getWidth(), getHeight());
        Graphics dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D gd=(Graphics2D) g.create();
        gd.setColor(Color.blue);
        gd.fill(sq1);
    }*/

}
