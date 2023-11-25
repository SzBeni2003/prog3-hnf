package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MenuWindow extends JFrame {
    JButton untangle=new JButton("Play Untangle");
    JButton twiddle=new JButton("Play Twiddle");
    JButton exit=new JButton("Exit game");
    public MenuWindow(){
        super("Menu");
        setSize(600,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        add(untangle);
        add(twiddle);
        add(exit);
        exit.addActionListener(e -> Main.menuWindow.dispatchEvent(new WindowEvent(Main.menuWindow, WindowEvent.WINDOW_CLOSING)));
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                Main.untangle.saveGame();
                Main.twiddle.saveGame();
            }
        });
        untangle.addActionListener(e -> {
            Main.menuWindow.setVisible(false);
            Main.gameWindow.openGame(Main.untangle);
            Main.gameWindow.setVisible(true);
        });
        twiddle.addActionListener(e->{
            Main.menuWindow.setVisible(false);
            Main.gameWindow.openGame(Main.twiddle);
            Main.gameWindow.setVisible(true);
        });
    }

}
