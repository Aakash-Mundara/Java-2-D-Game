package objects;

import main.Game;

import javax.swing.*;
import static utilz.constant.ObjectConstants.*;

public class GameContainer extends GameObject {
    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox() {
        if (objType == Box) {
            initHitbox(25, 18);
            yDrawOffset = (int) (12 * Game.Scale);
            xDrawOffset = (int) (7 * Game.Scale);
        } else {
            initHitbox(23, 25);
            xDrawOffset = (int) (8 * Game.Scale);
            yDrawOffset = (int) (5 * Game.Scale);
        }
        hitbox.y += yDrawOffset + (int) (Game.Scale * 2);
        hitbox.x += xDrawOffset / 2;
    }
    public void update(){
        if(doAnimation)
            updateAnimationTick();
    }
}
