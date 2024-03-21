package gamestates;

import levels.LevelManager;
import entities.EnemyManager;
import entities.Player;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverplay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utilz.constant.Enviroment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private boolean paused = false;
    private PauseOverplay pauseOverplay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private int xLvlOffset;
    private int leftBoarder = (int)(0.2 * Game.Game_Width);
    private int rightBoarder = (int)(0.8 * Game.Game_Width);
    private int maxLvlOffsetX;
    private BufferedImage backgroundImg,bigCloud,smallCloud;
    private int[] smallCloudPos;
    private Random rnd = new Random();
    private boolean gameOver;
    private boolean levleCompleted;
    private boolean playerDying;
    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtLas(LoadSave.Playing_Bg_Img);
        bigCloud = LoadSave.GetSpriteAtLas(LoadSave.Big_Clouds);
        smallCloud = LoadSave.GetSpriteAtLas(LoadSave.Small_Clouds);
        smallCloudPos = new int[8];
        for(int i = 0;i< smallCloudPos.length;i++)
            smallCloudPos[i] = (int) ( 90 * Game.Scale) + rnd.nextInt((int) (100 * Game.Scale));
        calcLvlOffset();
        loadStartLevel();
    }
    public void loadNextLevel(){
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {

        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(200,200,(int) (60*Game.Scale),(int) (40*Game.Scale),this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverplay = new PauseOverplay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);

    }
    @Override
    public void update() {
        if(paused){
            pauseOverplay.update();
        } else if(levleCompleted){
            levelCompletedOverlay.update();
        } else if(gameOver){
           gameOverOverlay.update();
        }else if(playerDying){
            player.update();
        }else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(),player);
            checkCloseToBoarder();
        }

    }

    private void checkCloseToBoarder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;
        if(diff > rightBoarder)
            xLvlOffset += diff - rightBoarder;
        else if(diff < leftBoarder)
            xLvlOffset += diff - leftBoarder;
        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if(xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg,0,0,Game.Game_Width,Game.Game_Height,null);
        drawClouds(g);
        levelManager.draw(g,xLvlOffset);
        player.render(g,xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g,xLvlOffset);
        if(paused){
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,Game.Game_Width,Game.Game_Height);
            pauseOverplay.draw(g);
        }else if(gameOver)
            gameOverOverlay.draw(g);
        else if(levleCompleted)
            levelCompletedOverlay.draw(g);


    }

    private void drawClouds(Graphics g) {
        for(int i = 0;i<3;i++)
            g.drawImage(bigCloud, i * Big_Cloud_Width - (int)(xLvlOffset * 0.3) ,(int)(204 * Game.Scale),Big_Cloud_Width,Big_Cloud_Height,null);
        for(int i = 0; i< smallCloudPos.length;i++)
            g.drawImage(smallCloud,Small_Cloud_Width * 4  * i - (int)(xLvlOffset * 0.7) ,smallCloudPos[i],Small_Cloud_Width,Small_Cloud_Height,null);
    }
    public void resetAll(){
        gameOver = false;
        paused = false;
        levleCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();

    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
    public void checkEnemyHit(Rectangle2D.Float attackBox){

        enemyManager.checkEnemyHit(attackBox);
    }
    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);

    }
    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }
    public void checkObjectHit(Rectangle2D.Float attackBox){
        objectManager.checkObjectHit(attackBox);
    }

    public void mouseDragged(MouseEvent e){
        if(!gameOver)
            if(paused)
                pauseOverplay.mouseDragged(e);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver)
            if(e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
            }


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverplay.mousePressed(e);
            else if (levleCompleted)
                levelCompletedOverlay.mousePressed(e);
        }else{
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverplay.mouseReleased(e);
            else if (levleCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }else{
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverplay.mouseMoved(e);
            else if (levleCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }else{
            gameOverOverlay.mouseMoved(e);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_F:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
            switch (e.getKeyCode()){
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_F:
                    player.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }


    }
    public void setLevelCompleted(boolean lvlCompleted){
        this.levleCompleted =lvlCompleted;
    }
    public void setMaxLvlOffset(int lvlOffset){
        this.maxLvlOffsetX = lvlOffset;
    }
    public void unpauseGame(){
        paused =false;
    }
    public void windowFocusLost(){
        player.resetDirBooleans();
    }
    public Player getPlayer(){
        return player;
    }
    public EnemyManager getEnemyManager(){
        return enemyManager;
    }
    public ObjectManager getObjectManager(){
        return objectManager;
    }
    public LevelManager getLevelManager(){
        return levelManager;
    }
    public void setPlayerDying(boolean b){
        this.playerDying = playerDying;
    }


}
