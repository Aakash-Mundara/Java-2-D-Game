package utilz;
import entities.Crabby;
import main.Game;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.constant.ObjectConstants.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    if (!IsSolid(x, y + height, lvlData))
                        return true;


        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.Tiles_Size;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.Game_Height)
            return true;
        float xIndex = x / Game.Tiles_Size;
        float yIndex = y / Game.Tiles_Size;
        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);

    }
    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);

    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];
        if (value >= 48 || value < 0 || value != 11)
            return true;
        return false;

    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int CurrentTile = (int) (hitbox.x / Game.Tiles_Size);
        if (xSpeed > 0) {
            int tileXPos = CurrentTile * Game.Tiles_Size;
            int XOffset = (int) (Game.Tiles_Size - hitbox.width);
            return tileXPos + XOffset - 1;
        } else {
            return CurrentTile * Game.Tiles_Size;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int CurrentTile = (int) (hitbox.y / Game.Tiles_Size);
        if (airSpeed > 0) {
            int tileYPos = CurrentTile * Game.Tiles_Size;
            int YOffset = (int) (Game.Tiles_Size - hitbox.height);
            return tileYPos + YOffset - 1;

        } else
            return CurrentTile * Game.Tiles_Size;


    }

    public static boolean IsEntityOneFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);

    }
    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.Tiles_Size);
        int secondXTile = (int) (secondHitbox.x / Game.Tiles_Size);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }


    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd,y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }

        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox,
                                       Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.Tiles_Size);
        int secondXTile = (int) (secondHitbox.x / Game.Tiles_Size);
        if (firstXTile > secondXTile) {
            return IsAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return IsAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
        }

    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Crabby> GetCrabbs(BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == constant.EnemyConstants.Crabby)
                    list.add(new Crabby(i * Game.Tiles_Size, j * Game.Tiles_Size));
            }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(i * Game.Tiles_Size, j * Game.Tiles_Size);
            }
        return new Point(1 * Game.Tiles_Size,1 * Game.Tiles_Size);
    }
    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == Red_Potion || value == Blue_Potion)
                    list.add(new Potion(i * Game.Tiles_Size, j * Game.Tiles_Size,value));
            }
        return list;
    }
    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == Box || value == Barrel)
                    list.add(new GameContainer(i * Game.Tiles_Size, j * Game.Tiles_Size,value));
            }
        return list;
    }
    public static ArrayList<Spike> GetSpikes(BufferedImage img){
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SPIKE)
                    list.add(new Spike(i * Game.Tiles_Size, j * Game.Tiles_Size,SPIKE));
            }
        return list;

    }
    public static ArrayList<Cannon> GetCannons(BufferedImage img){
        ArrayList<Cannon> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == CANNON_LEFT || value == CANNON_RIGHT)
                    list.add(new Cannon(i * Game.Tiles_Size, j * Game.Tiles_Size,value));
            }
        return list;

    }
}
