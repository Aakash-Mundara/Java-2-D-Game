package main;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import utilz.LoadSave;

import java.awt.*;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel ;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Playing playing;
    private Menu menu;
    public final static int Tiles_Default_Size = 32;
    public final static float Scale = 1.5f;
    public final static int Tiles_In_Width = 26;
    public final static int Tiles_In_Height = 14;
    public final static int Tiles_Size = (int) (Tiles_Default_Size * Scale);
    public final static int Game_Width = Tiles_Size * Tiles_In_Width;
    public final static int Game_Height = Tiles_Size * Tiles_In_Height;
    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);

    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update(){
        switch (Gamestate.state){
            case MENU:
                menu.update();
                break;

            case PLAYING :
                playing.update();
                break;
            case OPTIONS:
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g){


        switch (Gamestate.state){
            case MENU:
                menu.draw(g);
                break;

            case PLAYING :
                playing.draw(g);
                break;
            default:
                break;
        }

    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        double deltaF = 0;
        while(true){
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if(deltaU >= 1){
                update();
                updates++;
                deltaU--;
            }
            if(deltaF >=1  ){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
         //   if(now - lastFrame >= timePerFrame){
           //     gamePanel.repaint();
          //      lastFrame = now;
        //        frames++;
        //    }

            if(System.currentTimeMillis()-lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("Frames: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }

    }
    public void windowFocusLost(){
        if(Gamestate.state == Gamestate.PLAYING){
            playing.getPlayer().resetDirBooleans();
        }
    }
    public Menu getMenu(){
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }

}
