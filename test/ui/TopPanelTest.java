package ui;

import game.twiddle.Twiddle;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TopPanelTest {
    MenuWindow mw=new MenuWindow();
    GameWindow gw=new GameWindow();

    /*@Test
    public void gameRunning(){
        gw.
    }*/

    @AfterEach
    public void close(){
        Main.getMenuWindow().dispatchEvent(new WindowEvent(Main.getMenuWindow(),WindowEvent.WINDOW_CLOSING));
    }
}
