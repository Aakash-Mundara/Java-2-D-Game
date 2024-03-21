package levels;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;
    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();

    }
    public void loadNextLevel(){
        lvlIndex++;
        if(lvlIndex >= levels.size()){
            lvlIndex = 0;
            System.out.println("No More Levels!  Game Completed");
            Gamestate.state = Gamestate.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img: allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtLas(LoadSave.Level_AtLas);
        levelSprite = new BufferedImage[48];
        for(int j = 0; j<4;j++){
            for(int i = 0;i<12;i++){
                int index = j*12+i;
                levelSprite[index] = img.getSubimage(i*32,j*32,32,32);
            }
        }
    }

    public void draw(Graphics g , int lvlOffset){
        g.drawImage(levelSprite[2],0,0,null);
        for(int j = 0;j<Game.Tiles_In_Height;j++){
            for(int i = 0;i<levels.get(lvlIndex).getLevelData()[0].length;i++){
                int index = levels.get(lvlIndex).getSpriteIndex(i,j);
                g.drawImage(levelSprite[index], Game.Tiles_Size * i - lvlOffset,Game.Tiles_Size*j,Game.Tiles_Size,Game.Tiles_Size,null);
            }
        }

    }
    public void update(){

    }
    public Level getCurrentLevel(){
        return levels.get(lvlIndex);
    }
    public int getAmountOfLevels(){
        return levels.size();
    }
}
