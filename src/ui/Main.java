package ui;

import untangle.*;
import twiddle.*;
public class Main {
    static MenuWindow menuWindow=new MenuWindow();
    static GameWindow untangleWindow=new GameWindow("Untangle",new Untangle());
    static GameWindow twiddleWindow=new GameWindow("Twiddle", new Twiddle(2));

    public static void main(String[] args) {
        menuWindow.show();

    }
}