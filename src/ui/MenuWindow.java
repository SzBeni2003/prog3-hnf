package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        exit.addActionListener(new ExitButtonListener());
        untangle.addActionListener(new UntangleListener());
    }

    static class ExitButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Main.menuWindow.dispatchEvent(new WindowEvent(Main.menuWindow, WindowEvent.WINDOW_CLOSING));
        }
    }
    static class UntangleListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Main.menuWindow.setVisible(false);
            Main.untangleWindow.setVisible(true);
        }
    }
}
