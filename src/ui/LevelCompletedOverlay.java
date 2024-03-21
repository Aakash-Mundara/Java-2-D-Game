package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utilz.constant.UI.UrmButtons.*;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu,next;
    private BufferedImage img;
    private int bgX,bgY,bgW,bgH;
    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int)(330 * Game.Scale);
        int nextX = (int)(445 * Game.Scale);
        int y = (int)(195 * Game.Scale);
        menu = new UrmButton(menuX,y,Urm_Size,Urm_Size,2);
        next = new UrmButton(nextX, y, Urm_Size,Urm_Size,0);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtLas(LoadSave.Completed_Img);
        bgW = (int)(img.getWidth() * Game.Scale);
        bgH = (int)(img.getHeight()* Game.Scale);
        bgX = Game.Game_Width / 2 - bgW /2;
        bgY = (int)(75 * Game.Scale);
    }
    public void draw(Graphics g){
        g.drawImage(img,bgX,bgY,bgW,bgH,null);
        next.draw(g);
        menu.draw(g);

    }
    public void update(){
        next.update();
        menu.update();

    }
    private boolean isIn(UrmButton b,MouseEvent e){
        return b.getBounds().contains(e.getX(),e.getY());
    }
    public void mouseMoved(MouseEvent e){
        next.setMouseOver(false);
        menu.setMouseOver(false);
        if(isIn(menu, e))
            menu.setMouseOver(true);
        else if(isIn(next ,e))
            next.setMouseOver(true);

    }
    public void mouseReleased(MouseEvent e){
        if(isIn(menu, e)) {
            if (menu.isMousePressed()){
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }

        }
        else if(isIn(next ,e))
            if(next.isMousePressed())
                playing.loadNextLevel();
        menu.resetBools();
        next.resetBools();
    }
    public void mousePressed(MouseEvent e){
        if(isIn(menu, e))
            menu.setMousePressed(true);
        else if(isIn(next ,e))
            next.setMousePressed(true);

    }

}
