package ui;

import game.Game;
import game.twiddle.Twiddle;
import game.untangle.Untangle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TopPanel extends JMenuBar {
    JMenuItem back=new JMenuItem("Back");
    JLabel GameName;
    JMenu options=new JMenu("Options");
    JMenuItem newGame=new JMenuItem("New Game");
    JMenuItem restart=new JMenuItem("Restart");
    JMenu settings=new JMenu("Settings");
    HashMap<JMenuItem,?>  sizeOptions=new HashMap<>();
    TopPanel(){
        super();
        GameName=new JLabel("Game");
        back.addActionListener(e->{
            Main.gameWindow.setVisible(false);
            Main.menuWindow.setVisible(true);
        });
        add(back);
        add(GameName,CENTER_ALIGNMENT);
        add(options);
        add(settings);
        options.add(newGame);
        options.add(restart);

        newGame.addActionListener(e->{
            Main.gameWindow.getGameField().generateGame();
        });
        restart.addActionListener(e->{
            Main.gameWindow.getGameField().loadGame();
        });
    }
    public void setName(String string){
        GameName.setText(string);
    }
    public void setSizeOptions(HashMap<JMenuItem,?> options){
        for(JMenuItem option:sizeOptions.keySet()){
            settings.remove(option);
        }
        sizeOptions=options;
        ArrayList<JMenuItem> items= new ArrayList<>(sizeOptions.keySet().stream().toList());
        Collections.sort(items, Comparator.comparing(JMenuItem::getText));
        for(JMenuItem option: items){
            settings.add(option);
            option.addActionListener(e->{
                if(Main.getGameWindow().getGameField().getGameType() == Twiddle.class){
                    Main.twiddle.generateGame((Twiddle.TwiddleMove) sizeOptions.get(option));
                }else if(Main.getGameWindow().getGameField().getGameType() == Untangle.class){
                    Main.untangle.generateGame((int) sizeOptions.get(option));
                }
            });
        }
    }
}
