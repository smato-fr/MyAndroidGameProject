package fr.smato.gameproject.game.map.levels;

import android.graphics.Bitmap;

import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.map.Level;


public class WaitingRoomLevel extends Level {


    public WaitingRoomLevel(WaitingGameView gameView) {
        super(gameView, 20, 10, new Bitmap[]{
                TEXTURE_GROUND,
                TEXTURE_WALL
        }, "Salle d'attente");
    }

    @Override
    public void init() {

        for (int x = 0; x < mapWidht; x++) {
            tileMap[0][x] = 1;
            tileMap[mapHeight-1][x] = 1;
        }

        for (int y = 1; y < mapHeight; y++) {
            tileMap[y][0] = 1;
            tileMap[y][mapWidht-1] = 1;
        }

    }

    @Override
    public void update() {

    }
}
