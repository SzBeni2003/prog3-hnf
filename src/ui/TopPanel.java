package ui;

import game.twiddle.Twiddle;
import game.untangle.Untangle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Represents the top panel containing menu items and options in the game window.
 */
public class TopPanel extends JMenuBar {

    /**
     * The menu item to navigate back to the main menu.
     */
    final JMenuItem back = new JMenuItem("Back");

    /**
     * The label displaying the name of the game.
     */
    final JLabel GameName;

    /**
     * The menu for game options.
     */
    final JMenu options = new JMenu("Options");

    /**
     * The menu item for starting a new game.
     */
    final JMenuItem newGame = new JMenuItem("New Game");

    /**
     * The menu item for restarting the current game.
     */
    final JMenuItem restart = new JMenuItem("Restart");

    final JMenuItem solve=new JMenuItem("Show step-by-step solution");

    /**
     * The menu for settings.
     */
    final JMenu settings = new JMenu("Settings");

    /**
     * HashMap storing size options for the game.
     */
    HashMap<JMenuItem, ?> sizeOptions = new HashMap<>();

    JMenuItem customOption = new JMenuItem("Custom:");

    /**
     * Constructs the TopPanel, initializing its components and actions.
     */
    TopPanel() {
        super();
        GameName = new JLabel("Game");
        back.addActionListener(e -> {
            Main.gameWindow.setVisible(false);
            Main.menuWindow.setVisible(true);
        });
        add(back);
        add(GameName);
        add(options);
        add(settings);
        options.add(newGame);
        options.add(restart);
        options.add(solve);

        newGame.addActionListener(e -> Main.gameWindow.getGameField().generateGame());
        restart.addActionListener(e -> Main.gameWindow.getGameField().restart());
        solve.addActionListener(e->{
            if(Main.getGameWindow().getGameField().getGameType() == Untangle.class){
                Main.untangle.solve();
            }
        });
        customOption.addActionListener(e -> {
            JDialog custom = new JDialog();
            custom.setSize(300, 150);
            Point pos = Main.getGameWindow().getLocationOnScreen();
            Dimension screenSize = Main.getGameWindow().getSize();
            int x = (screenSize.width - 300) / 2;
            int y = (screenSize.height - 200) / 2;
            custom.setLocation(x + pos.x, y + pos.y);

            JPanel top = new JPanel();
            JLabel text = new JLabel("Set the size you want to play on:");
            JPanel bot = new JPanel();
            JTextField textField = new JTextField(10);
            JButton submit = new JButton("Submit");
            top.add(text);
            bot.add(textField);
            JCheckBox checkBox = new JCheckBox();
            if (Main.getGameWindow().getGameField().getGameType() == Twiddle.class) {
                bot.add(checkBox);
            }
            bot.add(submit);
            custom.add(top, BorderLayout.NORTH);
            custom.add(bot, BorderLayout.SOUTH);
            submit.addActionListener(a -> {
                if (Main.getGameWindow().getGameField().getGameType() == Twiddle.class) {
                    Main.twiddle.generateGame(Integer.parseInt(textField.getText()), checkBox.isSelected());
                } else if (Main.getGameWindow().getGameField().getGameType() == Untangle.class) {
                    Main.untangle.generateGame(Integer.parseInt(textField.getText()));
                }
                custom.dispose();
            });
            custom.setVisible(true);
        });
    }

    /**
     * Sets the name of the game displayed in the panel.
     *
     * @param string The name of the game.
     */
    public void setName(String string) {
        GameName.setText(string);
    }

    /**
     * Sets the size options available in the settings menu.
     *
     * @param options The size options for the game.
     */
    public void setSizeOptions(HashMap<JMenuItem, ?> options) {
        for (JMenuItem option : sizeOptions.keySet()) {
            settings.remove(option);
        }
        settings.remove(customOption);
        sizeOptions = options;
        ArrayList<JMenuItem> items = new ArrayList<>(sizeOptions.keySet().stream().toList());
        items.sort(Comparator.comparing(JMenuItem::getText));
        for (JMenuItem option : items) {
            settings.add(option);
            option.addActionListener(e -> {
                if (Main.getGameWindow().getGameField().getGameType() == Twiddle.class) {
                    Main.twiddle.generateGame((Twiddle.TwiddleMove) sizeOptions.get(option));
                } else if (Main.getGameWindow().getGameField().getGameType() == Untangle.class) {
                    Main.untangle.generateGame((int) sizeOptions.get(option));
                }
            });
        }

        settings.add(customOption);

    }
}
