package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import  static utilz.constant.UI.UrmButtons.*;


public class UrmButton extends PauseButtons {
    private BufferedImage[] imgs;
    private int rowIndex,index;
    private boolean mouseOver,mousePressed;

    public UrmButton(int x, int y, int width, int height,int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImage();
    }

    private void loadImage() {
        BufferedImage temp = LoadSave.GetSpriteAtLas(LoadSave.Urm_Buttons);
        imgs = new BufferedImage[3];
        for(int i = 0;i< imgs.length;i++){
            imgs[i] = temp.getSubimage(i * Urm_Default_Size,rowIndex * Urm_Default_Size,Urm_Default_Size,Urm_Default_Size);

        }
    }
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;

    }
    public void draw(Graphics g){
        g.drawImage(imgs[index], x,y,Urm_Size,Urm_Size,null);

    }
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
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
}
