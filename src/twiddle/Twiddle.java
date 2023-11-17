package twiddle;

import javax.swing.*;
import java.awt.*;

public class Twiddle extends JPanel {
    static int size;
    JButton b1=new JButton();

    public Twiddle(int n){
        super();
        size=n;
        setLayout(new GridLayout(n,n));
    }
}
