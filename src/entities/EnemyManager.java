package entities;

import gamestates.Playing;
import levels.Level;
import ui.LevelCompletedOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.constant.EnemyConstants.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();
    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    public void update(int[][] lvlData,Player player){
        boolean isAnyActive = false;
        for (Crabby c: crabbies)
            if(c.isActive()) {
                c.update(lvlData, player);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }
    public void draw(Graphics g , int xLvlOffset){
        drawCrabs(g,xLvlOffset);

    }

    private void drawCrabs(Graphics g , int xLvlOffset) {
        for(Crabby c : crabbies) {
            if(c.isActive()){
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],
                        (int) c.getHitbox().x - xLvlOffset - Crabby_DrawOffset_X + c.flipX(),
                        (int) c.getHitbox().y - Crabby_DrawOffset_Y
                        , Crabby_Width * c.flipW(), Crabby_Height, null);
             //   c.drawAttackBox(g, xLvlOffset);
            }

        }
    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Crabby c: crabbies)
            if(c.isActive())
                if(attackBox.intersects(c.getHitbox())){
                    c.hurt(10);
                    return;
                }

    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtLas(LoadSave.Crabby_Sprite);
        for(int j = 0;j< crabbyArr.length;j++)
            for(int i = 0;i < crabbyArr[j].length;i++)
                crabbyArr[j][i] = temp.getSubimage(i * Crabby_Width_Default, j *Crabby_Height_Default,Crabby_Width_Default,Crabby_Height_Default);
    }
    public void resetAllEnemies(){
        for(Crabby c : crabbies)
            c.resetEnemy();
    }
}
