package fr.smato.gameproject.game.model.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import fr.smato.gameproject.R;
import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.model.objects.Location;

public class Entity {

    protected Context context;
    protected GameView game;

    protected Location location;
    protected Paint paint;

    protected int circleRadious;

    protected double vx, vy;


    public Entity(Context context, GameView game) {
        this.context = context;
        this.game = game;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        vx = 0;
        vy = 0;
    }

    public void resize(Location loc, Object...objects) {
        this.location = loc;
        this.circleRadious =  (int)  objects[0];

    }

    public void update() {


        if (vx != 0 || vy != 0) {
            double scaleX = 1d*game.getMapManager().getLevel().getMapWidht()/game.getScreenWidth();
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

            location.x+= vx;
            location.y+= vy;
        }

    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) location.x, (float) location.y, circleRadious, paint);
    }

    public boolean isTouched(double currentX, double currentY) {

        if (Math.sqrt(Math.pow(currentX - getX(), 2) + Math.pow(currentY - getY(), 2)) < circleRadious) {
            return true;
        }

        return false;
    }


    public double getX() {
        return location.x;
    }

    public double getY() {
        return location.y;
    }



    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

}
