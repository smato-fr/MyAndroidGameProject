package fr.smato.gameproject.game.model.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import fr.smato.gameproject.game.GameView;
import fr.smato.gameproject.game.model.utils.Dragable;
import fr.smato.gameproject.game.model.objects.Location;
import fr.smato.gameproject.utils.callback.Event;

public class JoyStick extends Entity implements Dragable {

    private Location innerCircleLocation;
    private int innerCircleRadious;
    private Paint innerCirclePaint;


    private boolean isPressed;
    private double actuatorX, actuatorY;

    private Event onMove;

    public JoyStick(Context context, GameView gameView) {
        super(context, gameView);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        super.paint.setColor(Color.GRAY);
        super.paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
    public void setOnMove(Event onMove) {
        this.onMove = onMove;
    }

    @Override
    public void resize(Location loc, Object... circleRadious2) {
        super.resize(new Location(loc.x, loc.y), circleRadious2);

        //circleRadious2:
        // 0 => outerCircleRadious
        // 1 => innerCircleRadious
        this.innerCircleLocation = loc;
        this.innerCircleRadious = (int) circleRadious2[1];
    }

    @Override
    public void update() {
        innerCircleLocation.x = super.location.x + actuatorX*super.circleRadious;
        innerCircleLocation.y = super.location.y + actuatorY*super.circleRadious;

        if (actuatorX  != 0 || actuatorY != 0) {
            onMove.onEvent();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawCircle((float) innerCircleLocation.x, (float) innerCircleLocation.y, innerCircleRadious, innerCirclePaint);
    }

    @Override
    public void onDrag() {
        isPressed = true;
    }

    @Override
    public void onDrop() {
        isPressed = false;
        actuatorX = 0;
        actuatorY = 0;
        onMove.onEvent();
    }

    @Override
    public boolean isDragging() {
        return isPressed;
    }

    public void setActuator(double currentX, double currentY) {
        double deltaX = currentX - super.getX();
        double deltaY = currentY - super.getY();
        double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (deltaDistance < super.circleRadious) {
            actuatorX = deltaX/super.circleRadious;
            actuatorY= deltaY/super.circleRadious;
        } else {
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
