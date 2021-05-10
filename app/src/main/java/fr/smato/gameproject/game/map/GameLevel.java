package fr.smato.gameproject.game.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.model.drawable.LevelEntity;
import fr.smato.gameproject.game.model.utils.Interactive;

public abstract class GameLevel extends Level {

    protected List<LevelEntity> entities = new ArrayList<>();

    public GameLevel(GameView gameView, Bitmap[] textures, boolean solids[], String roomName) {
        super(gameView, textures, solids, roomName);
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

    public void onInteract() {
        for (LevelEntity entity : entities)
            if (entity instanceof Interactive)
                ((Interactive) entity).onInteract();
    }
}
