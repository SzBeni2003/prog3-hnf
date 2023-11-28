package game.twiddle;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ui.Main;

import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import static org.junit.jupiter.api.Assertions.*;

public class TwidleTest {
    Twiddle twiddle;

    @Test
    public void setUp(){
        twiddle=new Twiddle(5,false);
        assertEquals(5, Twiddle.cells);
        assertEquals(25,twiddle.squares.size());
    }

    @AfterEach
    public void close(){
        Main.getMenuWindow().dispatchEvent(new WindowEvent(Main.getMenuWindow(),WindowEvent.WINDOW_CLOSING));
    }
}
