package objects;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.constant.Projectiles.*;

public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.Scale);
        int yOffset = (int) (5 * Game.Scale);

        if (dir == 1)
            xOffset = (int) (29 * Game.Scale);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    public void updatePos() {
        hitbox.x += dir * SPEED;
    }

    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
