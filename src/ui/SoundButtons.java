package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import static utilz.constant.UI.PauseButtons.*;

public class SoundButtons extends PauseButtons{
    private BufferedImage[][]  soundImgs;
    private boolean mouseOver,mousePressed;
    private boolean muted;
    private int rowIndex,colIndex;
    public SoundButtons(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtLas(LoadSave.Sound_Buttons);
        soundImgs = new BufferedImage[2][3];
        for(int j = 0;j< soundImgs.length;j++)
            for(int i  = 0; i< soundImgs[j].length;i++){
                soundImgs[j][i] = temp.getSubimage(i*Sound_Size_Default,j*Sound_Size_Default,Sound_Size_Default,Sound_Size_Default);
            }
    }
    public void update(){
        if(muted)
            rowIndex = 1;
        else
            rowIndex = 0;
        colIndex = 0;
        if(mouseOver)
            colIndex = 1;
        if(mousePressed)
            colIndex = 2;

    }
    public void resetBools(){
        mousePressed = false;
        mouseOver = false;
    }
    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][colIndex],x,y,width,height,null);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
