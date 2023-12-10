package ui;

import game.untangle.*;
import game.twiddle.*;

public class Main {
    static final MenuWindow menuWindow = new MenuWindow();

    public static final Twiddle twiddle = new Twiddle();
    public static final Untangle untangle = new Untangle();

    static final GameWindow gameWindow = new GameWindow();

    public static void main(String[] args) {
        menuWindow.show();
    }

    public static GameWindow getGameWindow() {
        return gameWindow;
    }

    public static MenuWindow getMenuWindow() {
        return menuWindow;
    }
}