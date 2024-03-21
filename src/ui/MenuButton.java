package ui;

import gamestates.Gamestate;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.constant.UI.Buttons.*;

public class MenuButton {
    private int xPos,yPos,rowIndex,index;
    private int xOffsetCenter = B_Width / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver,mousePressed;
    private Rectangle bounds;
    public MenuButton(int xPos,int yPos,int rowIndex,Gamestate state){
        this.xPos  = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter,yPos,B_Width,B_Height);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtLas(LoadSave.Menu_Buttons);
        for(int i = 0;i< imgs.length; i++){
            imgs[i] = temp.getSubimage(i* B_Width_Default, rowIndex * B_Height_Default,B_Width_Default,B_Height_Default);
        }
    }
    public void draw(Graphics g){
        g.drawImage(imgs[index],xPos - xOffsetCenter,yPos,B_Width,B_Height,null);
    }
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
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
    public Rectangle getBounds(){
        return bounds;
    }
    public void applyGamestate(){
        Gamestate.state = state;
    }
    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }
}
