package fr.smato.gameproject.game.map.levels;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.map.GameLevel;
import fr.smato.gameproject.game.model.drawable.Chest;
import fr.smato.gameproject.game.model.drawable.LevelEntity;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.utils.Interactive;
import fr.smato.gameproject.utils.callback.Event;

public class FirstGameLevel extends GameLevel {

    private LevelEntity switchLevelEntity;


    private Chest chest1, chest2;

    public FirstGameLevel(GameView gameView) {
        super(gameView, new Bitmap[]{
               TEXTURE_GROUND,
                TEXTURE_WALL
        }, new boolean[]{false, true}, "Salle 1");
    }

    @Override
    public void init() {

        for (int x = 0; x < mapWidth; x++) {
            tileMap[0][x] = 1;
            tileMap[mapHeight-1][x] = 1;
        }

        for (int y = 1; y < mapHeight; y++) {
            tileMap[y][0] = 1;
            tileMap[y][mapWidth-1] = 1;
        }

        tileMap[mapHeight/2][mapWidth-1] = 0;
        tileMap[mapHeight/2-1][mapWidth-1] = 0;


        switchLevelEntity = new LevelEntity(GameView.INSTANCE.getContext(), GameView.INSTANCE, new Event() {
            @Override
            public void onEvent() {
                GameView.INSTANCE.changeLevel(new SecondGameLevel((GameView) gameView), 0.1f, 0.5f);
            }
        });
        switchLevelEntity.setVisible(false);

        super.entities.add(switchLevelEntity);


        chest1 = new Chest(GameView.INSTANCE.getContext(), GameView.INSTANCE);
        chest2 = new Chest(GameView.INSTANCE.getContext(), GameView.INSTANCE);

        entities.add(chest1);
        entities.add(chest2);
    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        super.resize(screenWidth, screenHeight);

        switchLevelEntity.resize(new Location(screenWidth*1.1f, screenHeight/2), (int) tileHeigth*2);
        chest1.resize(new Location(screenWidth/2, tileWidth*1.1), (int) tileWidth*2, (int) tileWidth,  (int)tileHeigth);
        chest2.resize(new Location(tileWidth*1.1, tileHeigth/2),  (int) tileWidth*2,  (int) tileWidth,  (int) tileHeigth);

    }

    @Override
    public void update() {
        super.update();

    }


}
