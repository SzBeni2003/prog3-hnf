package ui;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    TopPanel top=new TopPanel();
    Game gameField;
    BottomPanel bottom=new BottomPanel();
    public GameWindow(){
        super();
        setSize(new Dimension(600,750));
        setResizable(false);
        setVisible(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                Main.gameWindow.setVisible(false);
                Main.menuWindow.setVisible(true);
            }
        });
        gameField=Main.untangle;

        add(top, BorderLayout.NORTH);
        add(gameField,BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
    }
    public void openGame(Game game){
        remove(gameField);
        gameField=game;
        add(gameField,BorderLayout.CENTER);
    }

    public BottomPanel getBottom() {
        return bottom;
    }

    public Game getGameField() {
        return gameField;
    }
}
