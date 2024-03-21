package objects;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.constant.Ani_Speed;
import static utilz.constant.ObjectConstants.*;

public class GameObject {
    protected int x,y,objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation,active = true;
    protected int aniTick,aniIndex;
    protected int xDrawOffset,yDrawOffset;
    public GameObject(int x,int y,int objType){
        this.x = x;
        this.y = y;
        this.objType = objType;
    }
    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= Ani_Speed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(objType)){
                aniIndex = 0;
                if(objType == Barrel || objType == Box){
                    doAnimation = false;
                    active = false;
                }
                else if(objType == CANNON_LEFT || objType == CANNON_RIGHT)
                    doAnimation = false;
            }
        }
    }
    public void reset(){
        aniTick = 0;
        aniIndex = 0;
        active = true;
        if(objType == Barrel || objType == Box || objType == CANNON_LEFT || objType == CANNON_RIGHT)
            doAnimation = false;
        else
            doAnimation = true;
    }
    protected void initHitbox( int width ,int height) {
        hitbox = new Rectangle2D.Float( x, y, (int)(width * Game.Scale),(int)(height * Game.Scale));
    }
    public  void drawHitbox(Graphics g, int xLvlOffset){
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y,(int) hitbox.width,(int) hitbox.height);
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public void setxDrawOffset(int xDrawOffset) {
        this.xDrawOffset = xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public void setActive(boolean active){
        this.active = active;

    }
    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }
    public boolean isActive() {
        return active;
    }
    public int getAniIndex(){
        return aniIndex;
    }
    public int getAniTick(){
        return aniTick;
    }
}
