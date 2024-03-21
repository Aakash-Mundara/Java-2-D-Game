package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.constant.UI.Volume_Buttons.*;


public class VolumeButton extends PauseButtons{
    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private int buttonX,minX,maxX;
    private boolean mouseOver,mousePressed;
    public VolumeButton(int x, int y, int width, int height) {
        super(x+width/2, y, Volume_Width, height);
        bounds.x -= Volume_Width / 2;
        buttonX = x+ width/2;
        this.x = x;
        this.width = width;
        minX = x  + Volume_Width / 2;
        maxX = x + width - Volume_Width / 2;
        loadImage();

    }
    private void loadImage() {
        BufferedImage temp = LoadSave.GetSpriteAtLas(LoadSave.Volume_Buttons);
        imgs = new BufferedImage[3];
        for(int i = 0;i < imgs.length;i++)
            imgs[i] = temp.getSubimage(i* Volume_Default_Width,0,Volume_Default_Width,Volume_Default_Height);
        slider = temp.getSubimage(3*Volume_Default_Width,0,Slider_Default_Width,Volume_Default_Height);


    }
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;


    }
    public void draw(Graphics g){
        g.drawImage(slider,x,y,width,height,null);
        g.drawImage(imgs[index],buttonX - Volume_Width / 2,y, Volume_Width,height,null);


    }
    public void changeX(int x){
        if(x < minX)
            buttonX = minX;
        else if(x > maxX)
            buttonX = maxX;
        else
            buttonX = x;
        bounds.x = buttonX - Volume_Width / 2;
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

