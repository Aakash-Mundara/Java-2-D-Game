package entities;
import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.constant.Ani_Speed;
import static utilz.constant.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.constant.Directons.*;
import static utilz.constant.Gravity;

public abstract class Enemy extends Entity{
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeed = 0.35f * Game.Scale;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.Tiles_Size;
    protected  boolean active = true;
    protected boolean attackChecked;
    public Enemy(float x, float y, int width, int height,int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.Scale * 0.35f;
    }
    protected void firstUpdateCheck(int[][] lvlData){
        if (!IsEntityOneFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }
    protected void updateInAir(int[][] lvlData){
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += airSpeed;
            airSpeed += Gravity;
        }else{
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,Ani_Speed);
            tileY = (int)(hitbox.y / Game.Tiles_Size);
        }
    }
    protected void move(int[][] lvlData){
        float xSpeed = 0;
        if(walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox,xSpeed,lvlData)){
                hitbox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }
    protected void turnTowarsPlayer(Player player){
        if(player.hitbox.x > hitbox.x)
            walkDir= RIGHT;
        else
            walkDir = LEFT;
    }
    protected boolean canSeePlayer(int[][] lvlData,Player player){
        int PlayerTileY = (int)(player.getHitbox().y / Game.Tiles_Size);
        if(PlayerTileY == tileY)
            if(isPlayerInRange(player)){
                if(IsSightClear(lvlData,hitbox,player.hitbox,tileY))
                    return true;
            }
        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int)( Math.abs(player.hitbox.x - hitbox.x));
        return absValue <= attackDistance * 5;
    }
    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int)( Math.abs(player.hitbox.x - hitbox.x));
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }
    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(Dead);
        else
            newState(Hit);
    }
    protected void checkEnemyHit(Rectangle2D.Float attackBox,Player player) {
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType));
        attackChecked = true;
    }
    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= Ani_Speed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType,state)){
                aniIndex = 0;
                switch (state){
                    case Attack,Hit -> state = Idle;
                    case Dead -> active = false;
                }
            }
        }
    }
    public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAnimationTick();
    }
    private void updateMove(int[][] lvlData){
        if(firstUpdate) {
            if (!IsEntityOneFloor(hitbox, lvlData))
                inAir = true;
            firstUpdate = false;
        }
        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += Gravity;
            }else{
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,Ani_Speed);
            }
        }else{
            switch(state){
                case Idle:
                    state = Running;
                    break;
                case Running:
                    float xSpeed = 0;
                    if(walkDir == LEFT)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;
                    if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
                        if(IsFloor(hitbox,xSpeed,lvlData)){
                            hitbox.x += xSpeed;
                            return;
                        }
                    changeWalkDir();
                    break;
            }

        }

    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }
    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(Idle);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive(){
        return active;
    }
}
