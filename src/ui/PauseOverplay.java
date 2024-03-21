package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.constant.UI.PauseButtons.*;
import static utilz.constant.UI.UrmButtons.*;
import static utilz.constant.UI.Volume_Buttons.*;

public class PauseOverplay {
    private BufferedImage backgroundImg;
    private int bgX,bgY,bgW,bgH;
    private SoundButtons musicButtons, sfxButtons;
    private UrmButton menuB,replayB,unpausedB;
    private Playing playing;
    private VolumeButton volumeButtons;
    public PauseOverplay(Playing playing){
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
        createVolumeButtons();

    }

    private void createVolumeButtons() {
        int vX = (int)(309 * Game.Scale);
        int vY = (int)(278 * Game.Scale);
        volumeButtons = new VolumeButton(vX,vY,Slider_Width,Volume_Height);
    }

    private void createUrmButtons() {
        int menuX = (int)(313 * Game.Scale);
        int replayX = (int)(387 * Game.Scale);
        int unpausedX = (int)(462 * Game.Scale);
        int bY = (int)(325 * Game.Scale);

        menuB = new UrmButton(menuX,bY,Urm_Size,Urm_Size,2);
        replayB = new UrmButton(replayX,bY,Urm_Size,Urm_Size,1);
        unpausedB = new UrmButton(unpausedX,bY,Urm_Size,Urm_Size,0);
    }

    private void createSoundButtons() {
        int soundX = (int)(450 * Game.Scale);
        int musicY = (int)(140 * Game.Scale);
        int sfxY = (int)(186 * Game.Scale);
        musicButtons = new SoundButtons(soundX,musicY,Sound_Size,Sound_Size);
        sfxButtons = new SoundButtons(soundX,sfxY, Sound_Size,Sound_Size);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtLas(LoadSave.Pause_Background);
        bgW = (int) (backgroundImg.getWidth() * Game.Scale);
        bgH = (int) (backgroundImg.getHeight() * Game.Scale);
        bgX = Game.Game_Width /2 - bgW /2;
        bgY = (int) (25* Game.Scale);
    }

    public void update(){
        musicButtons.update();
        sfxButtons.update();
        menuB.update();
        replayB.update();
        unpausedB.update();
        volumeButtons.update();

    }
    public  void draw(Graphics g){
        g.drawImage(backgroundImg,bgX,bgY,bgW,bgH,null);
        musicButtons.draw(g);
        sfxButtons.draw(g);
        menuB.draw(g);
        replayB.draw(g);
        unpausedB.draw(g);
        volumeButtons.draw(g);

    }
    public void mouseDragged(MouseEvent e){
        if(volumeButtons.isMousePressed()){
            volumeButtons.changeX(e.getX());
        }

    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e,musicButtons))
            musicButtons.setMousePressed(true);
        else if(isIn(e,sfxButtons))
            sfxButtons.setMousePressed(true);
        else if(isIn(e,menuB))
            menuB.setMousePressed(true);
        else if(isIn(e,replayB))
            replayB.setMousePressed(true);
        else if(isIn(e,unpausedB))
            unpausedB.setMousePressed(true);
        else if(isIn(e,volumeButtons))
            volumeButtons.setMousePressed(true);

    }
    public void mouseReleased(MouseEvent e) {
        if(isIn(e,musicButtons)){
            if(musicButtons.isMousePressed())
                musicButtons.setMuted(!musicButtons.isMuted());
        }

        else if(isIn(e,sfxButtons)){
            if(sfxButtons.isMousePressed())
                sfxButtons.setMuted(!sfxButtons.isMuted());
        }
        else if(isIn(e,menuB)){
            if(menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }

        }
        else if(isIn(e,replayB)){
            if(replayB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }

        }
        else if(isIn(e,unpausedB)){
            if(unpausedB.isMousePressed())
                playing.unpauseGame();
        }
        musicButtons.resetBools();
        sfxButtons.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpausedB.resetBools();
        volumeButtons.resetBools();

    }
    public void mouseMoved(MouseEvent e) {
        musicButtons.setMouseOver(false);
        sfxButtons.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpausedB.setMouseOver(false);
        volumeButtons.setMouseOver(false);
        if (isIn(e, musicButtons))
            musicButtons.setMouseOver(true);
        else if (isIn(e, sfxButtons))
            sfxButtons.setMouseOver(true);
        else if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
           replayB.setMouseOver(true);
        else if (isIn(e, unpausedB))
            unpausedB.setMouseOver(true);
        else if (isIn(e, volumeButtons))
            volumeButtons.setMouseOver(true);


    }
    private boolean isIn(MouseEvent e,PauseButtons b ){
        return b.getBounds().contains(e.getX(),e.getY());

    }
}
