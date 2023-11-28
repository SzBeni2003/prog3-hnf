package game.untangle;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ui.Main;

import java.awt.event.WindowEvent;

import static org.junit.jupiter.api.Assertions.*;

public class UntangleTest {
    Untangle untangle;

    @Test
    public void setUp(){
        untangle=new Untangle(7);
        assertEquals(7,untangle.graph.vertices.size());
    }

    @Test
    public void intersects(){
        untangle=new Untangle(6);
        assertFalse(untangle.graph.intersects(new int[]{0,1},new int[]{1,2}));
        assertFalse(untangle.graph.intersects(new int[]{0,1},new int[]{4,5}));
    }

    @AfterEach
    public void close(){
        Main.getMenuWindow().dispatchEvent(new WindowEvent(Main.getMenuWindow(),WindowEvent.WINDOW_CLOSING));
    }
}
