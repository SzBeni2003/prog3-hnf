package ui;

import javax.swing.*;

/**
 * The bottom panel of the GameField, consists of an undo and a redo button.
 */
public class BottomPanel extends JPanel {
    JButton undo=new JButton("Undo");
    JButton redo=new JButton("Redo");
    BottomPanel(){
        add(undo);
        add(redo);
        undo.addActionListener(e->{
            Main.gameWindow.gameField.undo();
        });
        redo.addActionListener(e->{
            Main.gameWindow.gameField.redo();
        });
        undo.setEnabled(false);
        redo.setEnabled(false);
    }
    public void setUndo(boolean active){
        undo.setEnabled(active);
    }
    public void setRedo(boolean active){
        redo.setEnabled(active);
    }
}
