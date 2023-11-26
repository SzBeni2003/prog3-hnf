package game;

import javax.swing.*;
import java.io.File;
import java.io.Serializable;

public abstract class Game extends JPanel implements Serializable {

    public abstract void loadGame();
    public abstract void saveGame();
    public abstract void generateGame();

    public abstract void undo();
    public abstract void redo();

    public abstract Class<?> getGameType();
}
