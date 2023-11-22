package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel {
    JButton back=new JButton("Vissza");
    JButton GameSettings=new JButton("Beállítások");
    JButton newGame=new JButton("Elakadtál?");
    TopPanel(){
        super();
        back.addActionListener(e->{
            Main.gameWindow.setVisible(false);
            Main.gameWindow.closeGame();
            Main.menuWindow.setVisible(true);
        });

        add(back);
        add(GameSettings);
        add(newGame);

    }
}
