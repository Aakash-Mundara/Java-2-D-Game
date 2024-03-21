package objects;

import main.Game;

public class Cannon extends GameObject{
    private int tileY;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.Tiles_Size;
        initHitbox(40, 26);
        hitbox.x -= (int) (4 * Game.Scale);
        hitbox.y += (int) (6 * Game.Scale);
    }

    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }
}
