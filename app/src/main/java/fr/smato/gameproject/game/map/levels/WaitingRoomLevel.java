package fr.smato.gameproject.game.map.levels;

import android.graphics.Bitmap;

import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.map.Level;


public class WaitingRoomLevel extends Level {


    public WaitingRoomLevel(WaitingGameView gameView) {
        super(gameView, new Bitmap[]{
                TEXTURE_GROUND,
                TEXTURE_WALLH1,
                TEXTURE_WALLH2,
                TEXTURE_WALL
        }, new boolean[]{false, true, true, true}, "Salle d'attente");
    }

    @Override
    public void init() {

        for (int x = 0; x < mapWidth; x++) {
            if (x >= 1 && x < mapWidth-1) {
                tileMap[0][x] = 1;
                tileMap[1][x] = 2;
            }
            else
                tileMap[0][x] = 3;

            tileMap[mapHeight-1][x] = 3;
        }

        for (int y = 1; y < mapHeight; y++) {
            tileMap[y][0] = 3;
            tileMap[y][mapWidth-1] = 3;
        }

    }

    @Override
    public void update() {

    }
}
