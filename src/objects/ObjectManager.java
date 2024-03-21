package objects;

import entities.Player;
import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.IsProjectileHittingLevel;
import static utilz.constant.ObjectConstants.*;
import static utilz.constant.Projectiles.CANNON_BALL_HEIGHT;
import static utilz.constant.Projectiles.CANNON_BALL_WIDTH;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImg,containerImg;
    private BufferedImage spikesImg,cannonBallImg;
    private BufferedImage[] cannonsImg;
    private ArrayList<Potion> potions;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }
    public void checkSpikesTouched(Player p) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }
    public void checkObjectTouched(Rectangle2D.Float hitbox){
        for(Potion p : potions)
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }

    }
    public void applyEffectToPlayer(Potion p){
        if(p.getObjType() == Red_Potion)
            playing.getPlayer().changeHealth(Red_Potion_Value);
        else
            playing.getPlayer().changePower(Blue_Potion_Value);

    }
    public void checkObjectHit(Rectangle2D.Float attackbox){
        for(GameContainer gc: containers)
            if(gc.isActive() && !gc.doAnimation){
                if(gc.getHitbox().intersects(attackbox)){
                    gc.setAnimation(true);
                    int type = 0;
                    if(gc.getObjType() == Barrel)
                        type = 1;
                    potions.add(new Potion((int)(gc.getHitbox().x + gc.getHitbox().width / 2),
                            (int)(gc.getHitbox().y - gc.getHitbox().height / 2),
                            type));
                    return;
                }
            }

    }
    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons  = newLevel.getCannons();
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtLas(LoadSave.Potion_AtLas);
        potionImg = new BufferedImage[2][7];
        for(int j = 0;j<potionImg.length;j++)
            for(int i = 0;i < potionImg[j].length; i++)
                potionImg[j][i] = potionSprite.getSubimage(12 * i,16 * j,12,16);
        BufferedImage containerSprite = LoadSave.GetSpriteAtLas(LoadSave.Container_AtLas);
        containerImg = new BufferedImage[2][8];
        for(int j = 0;j<containerImg.length;j++)
            for(int i = 0;i < containerImg[j].length; i++)
                containerImg[j][i] = containerSprite .getSubimage(40 * i,30 * j,40,30);

        spikesImg = LoadSave.GetSpriteAtLas(LoadSave.Trap_AtLas);
        cannonsImg = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtLas(LoadSave.Cannon_AtLas);

        for (int i = 0; i < cannonsImg.length; i++)
            cannonsImg[i] = temp.getSubimage(i * 40, 0, 40, 26);
        cannonBallImg = LoadSave.GetSpriteAtLas(LoadSave.Cannon_Ball);
    }
    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData,player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles)
            if (p.isActive()){
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }

        }
    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.Tiles_Size * 5;
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;

        } else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }
    private void updateCannons(int[][] lvlData,Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
                                c.setAnimation(true);
                            }
            c.update();
            if(c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
    }

    private void shootCannon(Cannon c) {
        int dir =1;
        if(c.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int)c.getHitbox().x,(int) c.getHitbox().y,dir));
    }

    public void draw(Graphics g ,int xLvlOffset){
        drawPotions(g,xLvlOffset);
        drawContainers(g,xLvlOffset);
        drawTrap(g,xLvlOffset);
        drawCannons(g,xLvlOffset);
        drawProjectiles(g,xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);

    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }

            g.drawImage(cannonsImg[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    private void drawTrap(Graphics g, int xLvlOffset) {
        for (Spike s : spikes)
            g.drawImage(spikesImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p : potions)
            if(p.isActive()){
                int type = 0;
                if(p.getObjType() == Red_Potion)
                    type = 1;
                g.drawImage(potionImg[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset()-xLvlOffset),
                        (int)(p.getHitbox().y - p.getyDrawOffset()),Potion_Width,
                        Potion_Height,null);

            }

    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for(GameContainer gc : containers)
            if(gc.isActive()){
                int type = 0;
                if(gc.getObjType() == Barrel)
                    type = 1;
                g.drawImage(containerImg[type][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset()-xLvlOffset),
                        (int)(gc.getHitbox().y - gc.getyDrawOffset()),Container_Width,
                        Container_Height,null);
            }
    }
    public void resetAllObjects(){
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for(Potion p : potions)
            p.reset();
        for(GameContainer gc : containers)
            gc.reset();
        for(Cannon c: cannons)
            c.reset();

    }
}