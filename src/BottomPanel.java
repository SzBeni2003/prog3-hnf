import javax.swing.*;

public class BottomPanel extends JPanel {
    JButton undo=new JButton("Undo");
    JButton redo=new JButton("Redo");
    BottomPanel(){
        add(undo);
        add(redo);
    }
}
