package ui;

import untangle.*;
import twiddle.*;
public class Main {
    static MenuWindow menuWindow=new MenuWindow();
    static Untangle untangle=new Untangle();
    static GameWindow untangleWindow=new GameWindow("Untangle",untangle);
    static Twiddle twiddle=new Twiddle(2);
    static GameWindow twiddleWindow=new GameWindow("Twiddle", twiddle);

    public static void main(String[] args) {
        menuWindow.show();

    }
}