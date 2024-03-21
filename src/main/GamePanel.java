package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utilz.constant.PlayerCosntants.*;
import static utilz.constant.Directons.*;
import static main.Game.Game_Height;
import static main.Game.Game_Width;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private int frames = 0;
    private long lastCheck = 0;
    private Game game;

    public GamePanel(Game game) {
        mouseInputs = new MouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Game_Width, Game_Height);
        //setMinimumSize(size);
        setPreferredSize(size);
        System.out.println("size: "+Game_Width + " : "+Game_Height);

        //setMaximumSize(size);
    }
    public void updateGame(){
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }
    public Game getGame(){
        return game;
    }


}





