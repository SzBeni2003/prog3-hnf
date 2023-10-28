import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel {
    JButton back=new JButton("Vissza");
    JButton GameSettings=new JButton("Beállítások");
    JButton newGame=new JButton("Elakadtál?");
    TopPanel(){
        super();
        back.addActionListener(new BackListener());

        add(back);
        add(GameSettings);
        add(newGame);

    }

    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Main.untangleWindow.setVisible(false);
            Main.menuWindow.setVisible(true);
        }
    }
}
