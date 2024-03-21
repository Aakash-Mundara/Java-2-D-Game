package utilz;

import main.Game;

public class constant {
    public static final float Gravity = 0.04f * Game.Scale;
    public static final int Ani_Speed  = 25;
    public static class Projectiles{
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;

        public static final int CANNON_BALL_WIDTH = (int)(Game.Scale * CANNON_BALL_DEFAULT_WIDTH);
        public static final int CANNON_BALL_HEIGHT = (int)(Game.Scale * CANNON_BALL_DEFAULT_HEIGHT);
        public static final float SPEED = 0.75f * Game.Scale;
    }
    public static class ObjectConstants{
        public static final int Red_Potion = 0;
        public static final int Blue_Potion = 1;
        public static final int Barrel  = 2;
        public static final int Box = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int Red_Potion_Value = 15;
        public static final int Blue_Potion_Value = 10;
        public static final int Container_Width_Default = 40;
        public static final int Container_Height_Default = 30;
        public static final int Container_Width = (int)(Game.Scale * Container_Width_Default);
        public static final int Container_Height = (int)(Game.Scale * Container_Height_Default);
        public static final int Potion_Width_Default = 12;
        public static final int Potion_Height_Default = 16;
        public static final int Potion_Width = (int)(Game.Scale * Potion_Width_Default);
        public static final int Potion_Height = (int)(Game.Scale * Potion_Height_Default);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (Game.Scale * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (Game.Scale * SPIKE_HEIGHT_DEFAULT);
        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * Game.Scale);
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * Game.Scale);

        public static int GetSpriteAmount(int object_type){
            switch (object_type){
                case Red_Potion , Blue_Potion:
                    return 7;
                case Barrel, Box:
                    return 8;
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7;
            }
            return 1;
        }

    }
    public static class EnemyConstants{
        public static final int Crabby = 0;
        public static final int Idle = 0;
        public static final int Running = 1;
        public static final int Attack = 2;
        public static final int Hit = 3;
        public static final int Dead = 4;
        public static final int Crabby_Width_Default = 72;
        public static final int Crabby_Height_Default = 32;
        public static final int Crabby_Width = (int)(Crabby_Width_Default * Game.Scale);
        public static final int Crabby_Height = (int)(Crabby_Height_Default * Game.Scale);
        public static final int Crabby_DrawOffset_X = (int)(26 * Game.Scale);
        public static final int Crabby_DrawOffset_Y = (int)(9 * Game.Scale);
        public static int GetSpriteAmount(int enemy_type,int enemy_state){
            switch (enemy_type){
                case Crabby:
                    switch (enemy_state){
                        case Idle:
                            return 9;
                        case Running:
                            return 6;
                        case Attack:
                            return 7;
                        case Hit:
                            return 4;
                        case Dead:
                            return 5;
                    }
            }
            return 0;
        }
        public static int GetMaxHealth(int enemy_type){
            switch (enemy_type){
                case Crabby:
                    return 10;
                default:
                    return 1;
            }
        }
        public static int GetEnemyDmg(int enemy_type){
            switch (enemy_type){
                case Crabby:
                    return 15;
                default:
                    return 0;
            }

        }
    }
    public static class Enviroment{
        public static final int Big_Cloud_Width_Default = 448;
        public static final int Big_Cloud_Height_Default = 101;
        public static final int Small_Cloud_Width_Default = 74;
        public static final int Small_Cloud_Height_Default = 24;
        public static final int Big_Cloud_Width = (int)(Big_Cloud_Width_Default * Game.Scale);
        public static final int Big_Cloud_Height = (int)(Big_Cloud_Height_Default * Game.Scale);
        public static final int Small_Cloud_Width = (int)(Small_Cloud_Width_Default * Game.Scale);
        public static final int Small_Cloud_Height = (int)(Small_Cloud_Height_Default * Game.Scale);
    }
    public static class UI{
        public static class Buttons{
            public static final int B_Width_Default = 140;
            public static final int B_Height_Default = 56;
            public static final int B_Width = (int) (B_Width_Default * Game.Scale);
            public static final int B_Height = (int) (B_Height_Default * Game.Scale);
        }
        public static class PauseButtons{
            public static final int Sound_Size_Default = 42;
            public static final int Sound_Size = (int) (Sound_Size_Default * Game.Scale);
        }
        public static class UrmButtons{
            public static final int Urm_Default_Size = 56;
            public static final int Urm_Size = (int)(Urm_Default_Size * Game.Scale);
        }
        public static class Volume_Buttons{
            public static final int Volume_Default_Width = 28;
            public static final int Volume_Default_Height = 44;
            public static final int Slider_Default_Width = 215;
            public static final int Volume_Width = (int)(Volume_Default_Width * Game.Scale);
            public static final int Volume_Height = (int)(Volume_Default_Height * Game.Scale);
            public static final int Slider_Width = (int)(Slider_Default_Width * Game.Scale);

        }
    }
    public static class Directons{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    public static class PlayerCosntants{
        public static final int Idle = 0;
        public static final int Running =1;
        public static final int Jump = 2;
        public static final int falling = 3;
        public static final int Attack = 4;
        public static final int Hit = 5;

        public static final int Dead = 6;



        public static int GetSpriteAmount(int player_action){
            switch(player_action){
                case Dead:
                    return 8;
                case Running:
                    return 6;
                case Idle:
                    return 5;
                case Hit:
                    return 4;
                case Jump:
                case Attack:
                    return 3;
                case falling:
                default:
                    return 1;
            }
        }

    }
}
