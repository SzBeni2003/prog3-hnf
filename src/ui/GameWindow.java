package ui;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Represents the main game window that contains panels for top, game field, and bottom sections.
 */
public class GameWindow extends JFrame {

    /** The top panel in the game window. */
    TopPanel top=new TopPanel();

    /** The game field panel. */
    Game gameField;

    /** The bottom panel in the game window. */
    BottomPanel bottom=new BottomPanel();

    /**
     * Constructs a GameWindow initializing its components and layout.
     */
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

    /**
     * Opens a new game by replacing the existing game field with the provided game.
     *
     * @param game The new game to be displayed.
     */
    public void openGame(Game game){
        remove(gameField);
        gameField=game;
        add(gameField,BorderLayout.CENTER);
    }

    /**
     * Retrieves the bottom panel of the game window.
     *
     * @return The bottom panel of the game window.
     */
    public BottomPanel getBottom() {
        return bottom;
    }

    /**
     * Retrieves the game field displayed in the window.
     *
     * @return The game field displayed in the window.
     */
    public Game getGameField() {
        return gameField;
    }
}
