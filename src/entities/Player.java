package entities;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.constant.*;
import static utilz.constant.Directons.*;
import static utilz.constant.Directons.DOWN;
import static utilz.constant.PlayerCosntants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private boolean moving  =false,attacking = false;
    private boolean left,right,jump;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.Scale;
    private float yDrawOffset = 4 * Game.Scale;
    private float jumpSpeed = -2.25f * Game.Scale;
    private float fallSpeedAfterCollision = 0.5f * Game.Scale;
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(192 * Game.Scale);
    private int statusBarHeight = (int)(58 * Game.Scale);
    private int statusBarX = (int)(10 * Game.Scale);
    private int statusBarY = (int)(10 * Game.Scale);
    private int healthBarWidth = (int)(150 * Game.Scale);
    private int healthBarHeight = (int)(4 * Game.Scale);
    private int healthBarXStart = (int)(34 * Game.Scale);
    private int healthBarYStart = (int)(14 * Game.Scale);
    private int healthWidth = healthBarWidth;
    // AttackBox
    private Rectangle2D.Float attackBox;
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecked;
    private Playing playing;
    private int tileY = 0;
    public Player(float x, float y,int width,int height,Playing playing) {
        super(x, y,width,height);
        this.playing = playing;
        this.state = Idle;
        this.maxHealth = 100;
        this.currentHealth = 35;
        this.walkSpeed = Game.Scale * 1.0f;
        loadAnimation();
        initHitbox(20  , 27 );
        initAttackBox();
    }
    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(20 * Game.Scale),(int)(20 * Game.Scale));
    }

    public void update(){
        updateHealthBar();
        if(currentHealth <= 0){
            if (state != Dead) {
                state = Dead;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
            } else if (aniIndex == GetSpriteAmount(Dead) - 1 && aniTick >= Ani_Speed - 1) {
                playing.setGameOver(true);
            } else
                updateAnimationTick();

            return;
        }
        updateAtackBox();
        updatePos();
        if(moving) {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int)(hitbox.y / Game.Tiles_Size);
        }
        if(attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();

    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
    }

    private void updateAtackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.Scale * 10);
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.Scale * 10);

        }
        attackBox.y = hitbox.y + (Game.Scale * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float) maxHealth) * healthBarWidth);
    }


    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[state][aniIndex],
                (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
       // drawHitbox(g,lvlOffset);
       // drawAttackBox(g,lvlOffset);
        drawUI(g);


    }

    private void drawUI(Graphics g) {

        g.drawImage(statusBarImg,statusBarX,statusBarY,statusBarWidth,statusBarHeight,null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX,healthBarYStart + statusBarY,healthWidth,healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= Ani_Speed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking =false;
                attackChecked = false;
            }
        }
    }
    private void setAnimation(){
        int startAni = state;
        if(moving){
            state = Running;
        }
        else
            state = Idle;
        if(inAir){
            if(airSpeed < 0)
                state = Jump;

            else{
                state = falling;
            }
        }
        if(attacking){
            state = Attack;
            if(startAni != Attack){
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if(startAni != state){
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;
        if(jump){
            jump();
        }
       // if(!left && !right && !inAir)
        //    return;
        if(!inAir)
            if((! left && !right) || (right && left))
                return;
        float xSpeed = 0;
        if(left) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if(right ) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }
        if(!inAir){
            if(!IsEntityOneFloor(hitbox,lvlData)){
                inAir = true;
            }
        }
        if(inAir){
            if(CanMoveHere(hitbox.x,hitbox.y+ airSpeed,hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += Gravity;
                updateXPos(xSpeed);
            }else{
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                }else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        }else{
            updateXPos(xSpeed);
        }
        moving = true;

    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+ xSpeed,hitbox.y ,hitbox.width, hitbox.height,lvlData)){
            hitbox.x += xSpeed;
        }else{
            hitbox.x = GetEntityXPosNextToWall(hitbox,xSpeed);
        }
    }
    public void changeHealth(int value){
        currentHealth += value;
        if(currentHealth <= 0){
            currentHealth = 0;
        }else if(currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }
    public void kill(){
        currentHealth = 0;
    }
    public void changePower(int value){
        System.out.println("Added Power! ");
    }

    private void loadAnimation() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");

            BufferedImage img = LoadSave.GetSpriteAtLas(LoadSave.Player_AtLas);
            animations = new BufferedImage[7][8];
            for (int j = 0; j < animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) { // Fix the loop condition
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
                }
            }
            statusBarImg = LoadSave.GetSpriteAtLas(LoadSave.Status_Bar);


    }
    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!IsEntityOneFloor(hitbox,lvlData))
            inAir = true;

    }
    public void resetDirBooleans(){
        right = false;
        left = false;
    }
    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }
    public void resetAll(){
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = Idle;
        currentHealth = maxHealth;
        hitbox.x = x;
        hitbox.y = y;
        if(!IsEntityOneFloor(hitbox,lvlData))
            inAir = true;
    }
    public int getTileY(){
        return  tileY;
    }
}
