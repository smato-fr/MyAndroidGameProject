package fr.smato.gameproject.game.model.drawable;


import android.content.Context;
import android.graphics.Canvas;


import fr.smato.gameproject.game.GameLoopThread;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.utils.GameViewI;

public class PlayerEntity extends Entity {


    public static final double SPEED_MAX = 400/ GameLoopThread.MAX_UPS;





    public PlayerEntity(Context context, GameViewI gameView) {
        super(context, gameView);
    }

    @Override
    public void resize(Location loc, Object... circleRadious1) {
        super.resize(loc, circleRadious1);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }


}
