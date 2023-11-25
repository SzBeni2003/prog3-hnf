package ui;

import javax.swing.*;

public class TopPanel extends JPanel {
    JButton back=new JButton("Vissza");
    JButton GameSettings=new JButton("Beállítások");
    JButton newGame=new JButton("Elakadtál?");
    TopPanel(){
        super();
        back.addActionListener(e->{
            Main.gameWindow.setVisible(false);
            Main.menuWindow.setVisible(true);
        });

        add(back);
        add(GameSettings);
        add(newGame);
        newGame.addActionListener(e->{
            Main.gameWindow.getGameField().generateGame();
        });
    }
}
