package utilz;

import entities.Crabby;
import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import static utilz.constant.EnemyConstants.*;

public class LoadSave {
    public static final String Player_AtLas = "player_sprites.png";
    public static final String Level_AtLas = "outside_sprites.png";
  //  public static final String Level_One_Data = "level_one_data.png";
    public static final String Menu_Buttons = "button_atlas.png";
    public static final String Menu_Background = "menu_background.png";
    public static final String Pause_Background = "pause_menu.png";
    public static final String Sound_Buttons = "sound_button.png";
    public static final String Urm_Buttons = "urm_buttons.png";
    public static final String Volume_Buttons = "volume_buttons.png";
    public static final String Menu_Background_Img = "background_menu.png";
    public static final String Playing_Bg_Img = "playing_bg_img.png";
    public static final String Big_Clouds = "big_clouds.png";
    public static final String Small_Clouds = "small_clouds.png";
    public static final String Crabby_Sprite = "crabby_sprite.png";
    public static final String Status_Bar = "health_power_bar.png";
    public static final String Completed_Img = "completed_sprite.png";
    public static final String Potion_AtLas= "potions_sprites.png";
    public static final String Container_AtLas = "objects_sprites.png";
    public static final String Trap_AtLas = "trap_atlas.png";
    public static final String Cannon_AtLas = "cannon_atlas.png";
    public static final String Cannon_Ball = "ball.png";
    public static final String Death_Screen = "death_screen.png";

    public static BufferedImage GetSpriteAtLas(String filename){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;

    }
    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;
        try{
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];
        for(int i = 0;i< filesSorted.length;i++)
            for(int j = 0; j < files.length; j++){
                if(files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
            }
        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
        for(int i = 0;i< imgs.length; i++)
            try{
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();

            }

        return imgs;
    }

}
