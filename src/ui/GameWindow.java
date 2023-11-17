package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    TopPanel top=new TopPanel();
    JPanel gameField;
    BottomPanel bottom=new BottomPanel();
    public GameWindow(String game, JPanel field){
        super(game);
        gameField=field;
        setSize(600,800);
        setResizable(false);
        setVisible(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        add(top, BorderLayout.NORTH);
        add(gameField,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
    }

    static class CloseListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            Main.untangleWindow.setVisible(false);
            Main.menuWindow.setVisible(true);
        }
    }
}
