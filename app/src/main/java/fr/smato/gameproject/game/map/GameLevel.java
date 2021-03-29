package fr.smato.gameproject.game.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.game.model.drawable.LevelEntity;

public abstract class GameLevel extends Level {


    protected List<LevelEntity> entities = new ArrayList<>();

    public GameLevel(int mapWidth, int mapHeight, Bitmap[] textures, String roomName) {
        super(mapWidth, mapHeight, textures, roomName);
    }


    @Override
    public void update() {

        for (LevelEntity entity : entities) {
            entity.update();
        }

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        for (LevelEntity entity : entities) {
            entity.draw(canvas);
        }
    }
}
