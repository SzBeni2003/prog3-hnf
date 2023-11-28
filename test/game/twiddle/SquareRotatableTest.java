package game.twiddle;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ui.Main;

import java.awt.event.WindowEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SquareRotatableTest {
    SquareRotatable sq;
    Twiddle twiddle;

    @Test
    public void setSpot(){
        twiddle=new Twiddle(4,true);
        sq=new SquareRotatable(1);
        sq.setSpot(6);
        assertEquals(325,sq.x);
        assertEquals(200,sq.y);
    }
    @AfterEach
    public void finish(){
        Main.getMenuWindow().dispatchEvent(new WindowEvent(Main.getMenuWindow(),WindowEvent.WINDOW_CLOSING));Main.getMenuWindow().dispatchEvent(new WindowEvent(Main.getMenuWindow(), WindowEvent.WINDOW_CLOSING));
    }

    @Test
    public void setOrientation(){
        twiddle=new Twiddle(4,true);
        sq=new SquareRotatable(1);
        sq.setOrientation(7);
        assertEquals(3,sq.getOrientation());
        assertEquals(7*Math.PI/2,sq.theta);
    }


}
