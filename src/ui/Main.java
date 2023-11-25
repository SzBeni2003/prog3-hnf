package ui;

import game.untangle.*;
import game.twiddle.*;
public class Main {
    static MenuWindow menuWindow=new MenuWindow();

    static Twiddle twiddle=new Twiddle();
    static Untangle untangle=new Untangle();

    static GameWindow gameWindow=new GameWindow();

    public static void main(String[] args) {
        menuWindow.show();
    }

    public static GameWindow getGameWindow() {
        return gameWindow;
    }
}