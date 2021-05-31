package fr.smato.gameproject.game.model.drawable;


import android.content.Context;
import android.graphics.Canvas;


import java.util.HashMap;

import fr.smato.gameproject.DataBaseManager;
import fr.smato.gameproject.game.GameLoopThread;
import fr.smato.gameproject.game.WaitingGameView;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.game.model.utils.GameViewI;

public class PlayerEntity extends Entity {


    public static final double SPEED_MAX = 400/ GameLoopThread.MAX_UPS;

    public PlayerEntity(Context context, GameViewI gameView) {
        super(context, gameView);
        setTranslated(false);
    }

    @Override
    public void resize(Location loc, Object... circleRadious1) {
        super.resize(loc, circleRadious1);
    }

    @Override
    public void update() {

        if (vx != 0 || vy != 0) {
            double scaleX = 1d*game.getMapManager().getLevel().getMapWidth()/game.getScreenWidth();
            double scaleY = 1d*game.getMapManager().getLevel().getMapHeight()/game.getScreenHeight();

            int X = (int) ( (location.x) * scaleX);
            int Y = (int) ( (location.y) * scaleY);

            int x1 = (int) ( (location.x - circleRadious  + vx) *scaleX);
            int x2 = (int) ( (location.x + circleRadious+ vx) *scaleX);

            int y1 = (int) ( (location.y - circleRadious + vy) *scaleY);
            int y2 = (int) ( (location.y + circleRadious + vy) *scaleY);


            if (game.getMapManager().getLevel().isSolidTile(Y, x1) || game.getMapManager().getLevel().isSolidTile(Y, x2)) {
                vx = 0;
            }

            if (game.getMapManager().getLevel().isSolidTile(y1, X) || game.getMapManager().getLevel().isSolidTile(y2, X)) {
                vy = 0;
            }

            location.x+=vx;
            location.y+=vy;
            game.movePlayer();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(game.getScreenWidth()/2, game.getScreenHeight()/2, circleRadious, paint);
    }


    @Override
    public void setLocation(double x, double y) {
        location.x = x;
        location.y = y;
        game.movePlayer();
    }
}
