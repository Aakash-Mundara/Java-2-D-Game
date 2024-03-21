package entities;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.HelpMethods.*;
import static utilz.constant.Directons.LEFT;
import static utilz.constant.EnemyConstants.*;
import static utilz.constant.Directons.*;

public class Crabby extends Enemy{
    private int attackBoxOffsetX;
    public Crabby(float x, float y) {
        super(x, y, Crabby_Width, Crabby_Height, Crabby);
        initHitbox(22 , 19 );
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int) (82 * Game.Scale),(int)(19 * Game.Scale));
        attackBoxOffsetX = (int)(Game.Scale * 30);
    }

    public void update(int[][] lvlData,Player player){
        updateBehaviour(lvlData,player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehaviour(int[][] lvlData,Player player){
        if(firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if(inAir)
            updateInAir(lvlData);
        else{
            switch(state){
                case Idle:
                    newState(Running);
                    break;
                case Running:
                    if(canSeePlayer(lvlData,player)) {
                        turnTowarsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(Attack);
                    }
                    move(lvlData);
                    break;
                case Attack:
                    if(aniIndex == 0)
                        attackChecked = false;
                    if(aniIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox,player);
                    break;
                case Hit:
                    break;
            }

        }

    }
    public int flipX(){
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }
    public int flipW(){
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
