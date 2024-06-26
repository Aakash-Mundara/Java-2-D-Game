package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.constant.UI.UrmButtons.Urm_Size;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage img;
    private int imgX,imgY,imgW,imgH;
    private UrmButton menu,play;
    public GameOverOverlay(Playing playing){
        
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.Scale);
        int playX = (int) (440 * Game.Scale);
        int y = (int) (195 * Game.Scale);
        play = new UrmButton(playX, y, Urm_Size, Urm_Size, 0);
        menu = new UrmButton(menuX, y, Urm_Size, Urm_Size, 2);
    }

    private void createImg() {
        img = LoadSave.GetSpriteAtLas(LoadSave.Death_Screen);
        imgW = (int) (img.getWidth() * Game.Scale);
        imgH = (int) (img.getHeight() * Game.Scale);
        imgX = Game.Game_Width / 2 - imgW / 2;
        imgY = (int) (100 * Game.Scale);

    }

    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.Game_Width,Game.Game_Height);
        g.drawImage(img,imgX,imgY,imgW,imgH,null);
        menu.draw(g);
        play.draw(g);
        
      //  g.setColor(Color.WHITE);
      //  g.drawString("Game Over", Game.Game_Width / 2 , 150);
     //   g.drawString("Press esc to enter main menu!" , Game.Game_Width / 2,300);
    }
    public void update(){
        menu.update();
        play.update();
    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
    private boolean isIn(UrmButton b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(play, e))
            play.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        } else if (isIn(play, e))
            if (play.isMousePressed())
                playing.resetAll();

        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(play, e))
            play.setMousePressed(true);
    }
}
