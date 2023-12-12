package game;

import ui.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;

public abstract class Game extends JPanel implements Serializable {

    public abstract void loadGame() throws ClassNotFoundException, IOException;

    public abstract void saveGame();

    public abstract void generateGame();

    public abstract void undo();

    public abstract void redo();

    public void gameEnded() {
        JDialog popup = new JDialog(Main.getGameWindow(), "Game Ended");
        popup.setSize(300, 200);
        Point pos = Main.getGameWindow().getLocationOnScreen();
        Dimension screenSize = Main.getGameWindow().getSize();
        int x = (screenSize.width - 300) / 2;
        int y = (screenSize.height - 200) / 2;
        popup.setLocation(x + pos.x, y + pos.y);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        popup.add(panel);
        JLabel label = new JLabel("You solved the puzzle!");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(label);
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(a -> {
            generateGame();
            popup.dispose();
        });
        panel.add(newGame);
        JButton back = new JButton("Back to Menu");
        panel.add(back);
        back.addActionListener(a -> {
            popup.dispose();
            Main.getGameWindow().dispatchEvent(new WindowEvent(Main.getGameWindow(), WindowEvent.WINDOW_CLOSING));
        });
        popup.setVisible(true);
    }

    public abstract Class<?> getGameType();

    public abstract void restart();
}
