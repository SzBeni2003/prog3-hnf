package ui;

import game.twiddle.Twiddle;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class MenuWindow extends JFrame {
    HashMap<JMenuItem, Twiddle.TwiddleMove> twiddleOptions;
    HashMap<JMenuItem, Integer> untangleOptions;

    JButton untangle = new JButton("Play Untangle");
    JButton twiddle = new JButton("Play Twiddle");
    JButton exit = new JButton("Exit game");

    public MenuWindow() {
        super("Menu");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(untangle);
        add(twiddle);
        add(exit);
        exit.addActionListener(e -> Main.menuWindow.dispatchEvent(new WindowEvent(Main.menuWindow, WindowEvent.WINDOW_CLOSING)));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Main.untangle.saveGame();
                Main.twiddle.saveGame();
                Main.gameWindow.dispose();
            }
        });
        untangle.addActionListener(e -> {
            Main.menuWindow.setVisible(false);
            Main.gameWindow.openGame(Main.untangle);
            Main.gameWindow.top.setName("Untangle");
            Main.gameWindow.top.setSizeOptions(untangleOptions);
            Main.gameWindow.setSize(600, 750);
            Main.gameWindow.setVisible(true);
        });
        twiddle.addActionListener(e -> {
            Main.menuWindow.setVisible(false);
            Main.gameWindow.openGame(Main.twiddle);
            Main.gameWindow.top.setName("Twiddle");
            Main.gameWindow.top.setSizeOptions(twiddleOptions);
            Main.twiddle.setSizes();
            Main.gameWindow.setVisible(true);
        });

        twiddleOptions = new HashMap<>();
        twiddleOptions.put(new JMenuItem("3x3, normal"), new Twiddle.TwiddleMove(3, false));
        twiddleOptions.put(new JMenuItem("3x3, orientable"), new Twiddle.TwiddleMove(3, true));
        twiddleOptions.put(new JMenuItem("4x4, normal"), new Twiddle.TwiddleMove(4, false));
        twiddleOptions.put(new JMenuItem("4x4, orientable"), new Twiddle.TwiddleMove(4, true));
        twiddleOptions.put(new JMenuItem("5x5, normal"), new Twiddle.TwiddleMove(5, false));
        twiddleOptions.put(new JMenuItem("5x5, orientable"), new Twiddle.TwiddleMove(5, true));
        twiddleOptions.put(new JMenuItem("6x6, normal"), new Twiddle.TwiddleMove(6, false));
        twiddleOptions.put(new JMenuItem("6x6, orientable"), new Twiddle.TwiddleMove(6, true));

        untangleOptions = new HashMap<>();
        untangleOptions.put(new JMenuItem("6 points"), 6);
        untangleOptions.put(new JMenuItem("8 points"), 8);
        untangleOptions.put(new JMenuItem("10 points"), 10);
        untangleOptions.put(new JMenuItem("15 points"), 14);
        untangleOptions.put(new JMenuItem("20 points"), 20);
        untangleOptions.put(new JMenuItem("25 points"), 25);

    }

}
