package fr.smato.gameproject.game.map.levels;

import android.graphics.Bitmap;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.map.GameLevel;
import fr.smato.gameproject.game.model.drawable.LevelEntity;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.utils.callback.Event;

public class SecondGameLevel extends GameLevel {

    private LevelEntity switchLevelEntity;

    public SecondGameLevel() {
        super(20, 10, new Bitmap[]{WaitingGameView.loadImage(R.drawable.ground), WaitingGameView.loadImage(R.drawable.wall)}, "Salle 2");
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

        tileMap[mapHeight/2][0] = 0;
        tileMap[mapHeight/2-1][0] = 0;


        switchLevelEntity = new LevelEntity(GameView.INSTANCE.getContext(), GameView.INSTANCE, new Event() {
            @Override
            public void onEvent() {
                GameView.INSTANCE.changeLevel(new FirstGameLevel(), 0.9f, 0.5f);
            }
        });
        switchLevelEntity.setVisible(false);

        super.entities.add(switchLevelEntity);
    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        super.resize(screenWidth, screenHeight);

        switchLevelEntity.resize(new Location(-0.1f*screenWidth, screenHeight/2), (int) tileHeigth*2);
    }

    @Override
    public void update() {
        super.update();
    }
}
